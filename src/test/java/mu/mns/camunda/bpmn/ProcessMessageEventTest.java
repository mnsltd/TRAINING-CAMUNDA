package mu.mns.camunda.bpmn;


import mu.mns.camunda.config.AbstractTestContainersConfig;
import mu.mns.camunda.constant.WorkflowConstant;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static mu.mns.camunda.constant.MessageConstant.WAIT_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;
import static mu.mns.camunda.constant.DemoConstant.WORKFLOW_STATUS;
import static mu.mns.camunda.constant.MessageConstant.WORKFLOW_START_MESSAGE;

@SpringBootTest
@Deployment(resources = "bpmn/2_Message_event.bpmn")  // Path to your BPMN file
public class ProcessMessageEventTest extends AbstractTestContainersConfig {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    // Test case for the "Started" path
    @Test
    void testProcessExecution_workflowStatus_STARTED() {

        String businessKey = "Test1";
        // Start the process with workflow_status set to "STARTED"
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(WorkflowConstant.MESSAGE_EVENT_TASK_EXAMPLE, businessKey,
                Variables.createVariables().putValue(WORKFLOW_STATUS, "STARTED"));

        // Assert the process instance is not null
        assertThat(processInstance).isNotNull();

        Execution execution = runtimeService.createExecutionQuery().messageEventSubscription().singleResult();
        assertThat(execution).isNotNull();
        // Assert that the process has passed the decision gateway and is waiting at "Wait Response"
        BpmnAwareTests.assertThat(processInstance).isWaitingAt("WaitResponseTask");
        runtimeService.correlateMessage("wait-response", businessKey);

        // After completing the "Wait Response" task, assert the process moves to "Check Response"
        BpmnAwareTests.assertThat(processInstance).isWaitingAt("CheckResponseTask");

        // Complete the "Check Response" user task
        var task = taskService.createTaskQuery().taskDefinitionKey("CheckResponseTask").singleResult();
        assertThat(task).isNotNull();
        taskService.complete(task.getId());

        // Assert that the process has successfully reached the end event
        BpmnAwareTests.assertThat(processInstance).hasPassedInOrder("CheckResponseTask", "EndEvent_1");
    }

    // Test case for the "not Started" path
    @Test
    void testProcessExecution_workflowStatus_NOT_STARTED() {
        // Start the process with workflow_status set to "NOT_STARTED"
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage(WORKFLOW_START_MESSAGE, "Test2", Variables.createVariables().putValue(WORKFLOW_STATUS, "NOT_STARTED"));

        // Assert that the process instance is started and not null
        assertThat(processInstance).isNotNull();

        // Assert that the process has passed through the task
        BpmnAwareTests.assertThat(processInstance).hasPassed("WorkflowEndedTask");

        // Optionally assert the process has ended
        BpmnAwareTests.assertThat(processInstance).isEnded();
    }
}
