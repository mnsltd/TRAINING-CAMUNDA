package mu.mns.camunda.bpmn;


import mu.mns.camunda.config.AbstractTestContainersConfig;
import mu.mns.camunda.constant.MessageConstant;
import mu.mns.camunda.constant.WorkflowConstant;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static mu.mns.camunda.constant.DemoConstant.WORKFLOW_STATUS;
import static mu.mns.camunda.constant.MessageConstant.EVENT_CANCELLED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Deployment(resources = "bpmn/4_ExternalTask.bpmn")  // Path to your BPMN file
public class ProcessExternalTaskTest extends AbstractTestContainersConfig {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ExternalTaskService externalTaskService;

    // Test case for the "Started" path
    @Test
    void testProcessExecution_workflowStatus_STARTED() {

        String businessKey = "Test1";
        // Start the process with workflow_status set to "STARTED"
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(WorkflowConstant.EXTERNAL_TASK_EXAMPLE, businessKey);

        // Assert the process instance is not null
        assertThat(processInstance).isNotNull();

        runtimeService.correlateMessage(MessageConstant.WAIT_RESPONSE_EXTERNAL, businessKey,  Variables.createVariables()
                .putValue(WORKFLOW_STATUS, "Started"));

        BpmnAwareTests.assertThat(processInstance).isWaitingAt("ExternalTaskTask");
        ExternalTask externalTask = externalTaskService.createExternalTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        // There should be only one external task at this point
        assertThat(externalTask).isNotNull();
        externalTaskService.lock(externalTask.getId(), "testWorkerId", 10000L);  // Lock for 10 seconds

        Map<String, Object> variables = new HashMap<>();
        variables.put("outputVar", "externalTaskOk");
        externalTaskService.complete(externalTask.getId(), "testWorkerId", variables);

        // Assert that the process has passed the decision gateway and is waiting at "Wait Response"
        BpmnAwareTests.assertThat(processInstance).isWaitingAt("VerifyVariableReceivedTask");
        BpmnAwareTests.assertThat(processInstance).hasVariables("outputVar");
        String workflowStatus = (String) runtimeService.getVariable(processInstance.getId(), "outputVar");
        assertThat(workflowStatus).isEqualTo("externalTaskOk");

        var task = taskService.createTaskQuery().taskDefinitionKey("VerifyVariableReceivedTask").singleResult();
        taskService.complete(task.getId());


        BpmnAwareTests.assertThat(processInstance).hasPassedInOrder("EndEvent1");

        // After completing the "Wait Response" task, assert the process moves to "Check Response"
    }

    // Test case for the "not Started" path
    @Test
    void testProcessExecution_workflowStatus_NOT_STARTED() {
        // Start the process with workflow_status set to "NOT_STARTED"
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(WorkflowConstant.BOUNDARY_EVENT_EXAMPLE
                , "Test2", Variables.createVariables().putValue(WORKFLOW_STATUS, "NOT_STARTED"));

        // Assert that the process instance is started and not null
        assertThat(processInstance).isNotNull();

        // Assert that the process has passed through the task
        BpmnAwareTests.assertThat(processInstance).hasPassed("WorkflowEndedTask");

        // Optionally assert the process has ended
        BpmnAwareTests.assertThat(processInstance).isEnded();
    }
}
