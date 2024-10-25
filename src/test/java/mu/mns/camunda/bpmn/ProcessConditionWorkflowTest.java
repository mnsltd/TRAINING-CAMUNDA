package mu.mns.camunda.bpmn;


import mu.mns.camunda.config.AbstractTestContainersConfig;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.engine.test.junit5.ProcessEngineExtension;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static mu.mns.camunda.constant.DemoConstant.WORKFLOW_STATUS;
import static mu.mns.camunda.constant.MessageConstant.WORKFLOW_START_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;


@Deployment(resources = "bpmn/1_Conditional_flow.bpmn")
//@ExtendWith(ProcessEngineExtension.class)
@SpringBootTest
class ProcessConditionWorkflowTest extends AbstractTestContainersConfig {

    @Autowired
    RuntimeService runtimeService;  // Autowire Camunda's RuntimeService to start processes

    @Autowired
    TaskService taskService;

    @Test
    void testProcessExecution_workflowStatus_STARTED() {
        // Start the process by key using the BPMN process ID defined in simpleProcess.bpmn
        var processInstance = runtimeService.startProcessInstanceByMessage(WORKFLOW_START_MESSAGE,
                "Test1", Variables.createVariables().putValue(WORKFLOW_STATUS, "STARTED"));

        assertThat(mysql.isRunning()).isTrue();

        // Assert that the process instance was started successfully
        assertThat(processInstance).isNotNull();

        BpmnAwareTests.assertThat(processInstance).hasVariables(WORKFLOW_STATUS);
        BpmnAwareTests.assertThat(processInstance).isWaitingAt("UserTask1");  // Camunda-specific assertion


        // Fetch the user task by its definition key and complete it to proceed in the process
        var task = taskService.createTaskQuery().taskDefinitionKey("UserTask1").singleResult();
        taskService.complete(task.getId());

        // Assert that the process has successfully passed the end event
        BpmnAwareTests.assertThat(processInstance).hasPassedInOrder("EndEvent");
    }

    @Test
    void testProcessExecution_workflowStatus_NOT_STARTED() {
        // Start the process by key using the BPMN process ID defined in simpleProcess.bpmn
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage(WORKFLOW_START_MESSAGE, "Test2", Variables.createVariables().putValue(WORKFLOW_STATUS, "NOT_STARTED"));

        // Assert that the process instance is started and not null
        assertThat(processInstance).isNotNull();

        // Assert that the process has passed through the task
        BpmnAwareTests.assertThat(processInstance).hasPassed("WorkflowEndedTask");

        // Optionally assert the process has ended
        BpmnAwareTests.assertThat(processInstance).isEnded();

    }
}
