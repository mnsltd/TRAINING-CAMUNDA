package mu.mns.camunda.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mu.mns.camunda.constant.DemoConstant;
import mu.mns.camunda.constant.MessageConstant;
import mu.mns.camunda.constant.WorkflowConstant;
import mu.mns.camunda.dto.Order;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static mu.mns.camunda.constant.DemoConstant.WORKFLOW_STATUS;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProcessController {

    private final RuntimeService runtimeService;

    //Conditional Flow Example, Start Workflow With Message Event Example
    @GetMapping("/start-process/1")
    public String startProcess1() {
        // Start the process instance using the message "camunda-start-message"
        String businessKey = UUID.randomUUID().toString();
        runtimeService.startProcessInstanceByMessage(MessageConstant.WORKFLOW_START_MESSAGE, businessKey,
                Variables.createVariables().putValue(WORKFLOW_STATUS, "NOT_STARTED"));
        return "camunda process 1 started successfully with businessKey "+ businessKey;
    }

    //Message Event Example
    @GetMapping("/start-process/2")
    public String startProcess2() {
        // Start the process instance using the bppmn ID  "MessageEventTaskExample"
        String businessKey = UUID.randomUUID().toString();
        runtimeService.startProcessInstanceByKey("Demo", businessKey);
        return "camunda process MessageEventTaskExample started successfully with businessKey "+ businessKey;
    }

    //Boundary Event Example
    @GetMapping("/start-process/3")
    public String startProcess3() {
        // Start the process instance using the bppmn ID  "BoundaryEventExample"
        String businessKey = UUID.randomUUID().toString();
        runtimeService.startProcessInstanceByKey(WorkflowConstant.BOUNDARY_EVENT_EXAMPLE, businessKey, Variables.createVariables().putValue(WORKFLOW_STATUS, "STARTED"));
        return "camunda process BoundaryEventExample started successfully with businessKey "+ businessKey;
    }

    //External Task Example
    @GetMapping("/start-process/4")
    public String startProcess4() {
        // Start the process instance using the bppmn ID  "ExternalTaskExample"
        String businessKey = UUID.randomUUID().toString();
        runtimeService.startProcessInstanceByKey(WorkflowConstant.EXTERNAL_TASK_EXAMPLE, businessKey);
        return "camunda process ExternalTaskExample started successfully with businessKey "+ businessKey;
    }

    //Variables Example
    @GetMapping("/start-process/5")
    public String startProcess5() throws IOException {
        // Start the process instance using the bppmn ID  "Camunda5"
        String businessKey = UUID.randomUUID().toString();
        ClassPathResource resource = new ClassPathResource("case_study.pdf");
        // Get the File object from the resource
        File contractFile = ((org.springframework.core.io.Resource) resource).getFile();
        // Create a simple order object
        Order order = new Order("Laptop", 2, new BigDecimal(1500));

        // Store the order object as ObjectValue (serialized to JSON)
        ObjectValue orderValue = Variables.objectValue(order)
                .serializationDataFormat(Variables.SerializationDataFormats.JSON)
                .create();
        // Set a File variable (e.g., contract file)
        FileValue fileValue = Variables.fileValue("case_study.pdf")
                .file(contractFile)
                .mimeType("application/pdf")
                .create();

        // Create a map of variables to pass when starting the workflow
        VariableMap variables = Variables.createVariables()
                .putValueTyped(DemoConstant.ORDER_VARIABLE, orderValue) // ObjectValue for the order
                .putValueTyped(DemoConstant.CONTRACT_FILE, fileValue)   // FileValue for the contract
                .putValue(DemoConstant.STATUS, "Approved")            // String value
                .putValue(DemoConstant.QUANTITY, 5)                   // Integer value
                .putValue(DemoConstant.IS_APPROVED, true);             // Boolean value

        log.info("Process started with variables.");

        runtimeService.startProcessInstanceByKey(WorkflowConstant.VARIABLES_EXAMPLE, businessKey, variables);


        return "camunda process VariablesExample started successfully with businessKey "+ businessKey;
    }

    //CallActivity Example
    @GetMapping("/start-process/6")
    public String startProcess6() throws IOException {

        // Start the process instance using the bppmn ID  "Camunda5"
        String businessKey = UUID.randomUUID().toString();
        ClassPathResource resource = new ClassPathResource("case_study.pdf");
        // Get the File object from the resource
        File contractFile = ((org.springframework.core.io.Resource) resource).getFile();
        // Create a simple order object
        Order order = new Order("Laptop", 2, new BigDecimal(1500));

        // Store the order object as ObjectValue (serialized to JSON)
        ObjectValue orderValue = Variables.objectValue(order)
                .serializationDataFormat(Variables.SerializationDataFormats.JSON)
                .create();
        // Set a File variable (e.g., contract file)
        FileValue fileValue = Variables.fileValue("case_study.pdf")
                .file(contractFile)
                .mimeType("application/pdf")
                .create();

        // Create a map of variables to pass when starting the workflow
        VariableMap variables = Variables.createVariables()
                .putValueTyped(DemoConstant.ORDER_VARIABLE, orderValue) // ObjectValue for the order
                .putValueTyped(DemoConstant.CONTRACT_FILE, fileValue)   // FileValue for the contract
                .putValue(DemoConstant.STATUS, "Approved")            // String value
                .putValue(DemoConstant.QUANTITY, 5)                   // Integer value
                .putValue(DemoConstant.IS_APPROVED, true)            // Boolean value
                .putValue(WORKFLOW_STATUS, "STARTED");

        log.info("Process started with variables.");

        runtimeService.startProcessInstanceByKey(WorkflowConstant.CALL_ACTIVITY_EXAMPLE, businessKey, variables);

        return "camunda process CallActivityExample started successfully with businessKey "+ businessKey;
    }

    //TransactionExample
    @GetMapping("/start-process/7")
    public String startProcess7() {
        // Start the process instance using the bppmn ID  "Camunda7"
        String businessKey = UUID.randomUUID().toString();
        runtimeService.startProcessInstanceByKey(WorkflowConstant.TRANSACTION_EXAMPLE, businessKey);
        return "camunda process TransactionExample started successfully with businessKey "+ businessKey;
    }

    @GetMapping("/start-process/8")
    public String startProcess8() {
        // Start the process instance using the bppmn ID  "Camunda8"
        String businessKey = UUID.randomUUID().toString();
        runtimeService.startProcessInstanceByKey(WorkflowConstant.SIGNAL_EXAMPLE, businessKey);
        return "camunda process SignalExample started successfully with businessKey "+ businessKey;
    }


    @PostMapping("/message/{businessKey}")
    public void correlateMessage(@PathVariable("businessKey") String businessKey) {

        runtimeService.correlateMessage(MessageConstant.WAIT_RESPONSE, businessKey,  Variables.createVariables()
                .putValue(WORKFLOW_STATUS, "Started"));
        log.info("Message Started sent to process with businessKey {}", businessKey);
    }

    @PostMapping("/message/boundary/{businessKey}")
    public void correlateMessageBoundary(@PathVariable("businessKey") String businessKey) {

        runtimeService.correlateMessage(MessageConstant.WAIT_RESPONSE_BOUNDARY, businessKey,  Variables.createVariables()
                .putValue(WORKFLOW_STATUS, "Started"));
        log.info("Message wait-response-boundary sent to process with businessKey {}", businessKey);
    }

    @PostMapping("/message/external/{businessKey}")
    public void correlateMessageExternal(@PathVariable("businessKey") String businessKey) {

        runtimeService.correlateMessage(MessageConstant.WAIT_RESPONSE_EXTERNAL, businessKey,  Variables.createVariables()
                .putValue(WORKFLOW_STATUS, "Started"));
        log.info("Message Started sent to process with businessKey {}", businessKey);
    }

    @PostMapping("/message/external/not-started/{businessKey}")
    public void correlateMessageNotStarted(@PathVariable("businessKey") String businessKey) {

        runtimeService.correlateMessage(MessageConstant.WORKFLOW_NOT_STARTED, businessKey,  Variables.createVariables()
                .putValue(WORKFLOW_STATUS, "NOT_Started"));
        log.info("Message Not started sent to process with businessKey {}", businessKey);
    }

    @PostMapping("/message/cancel/{businessKey}")
    public void correlateCancelMessage(@PathVariable("businessKey") String businessKey) {


        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)  // Filter by process instance ID
                .messageEventSubscriptionName(MessageConstant.EVENT_CANCELLED)  // Check if waiting for this message
                .singleResult();

        if (null != execution) {
            runtimeService.createMessageCorrelation(MessageConstant.EVENT_CANCELLED)
                    .processInstanceBusinessKey(businessKey)
                    .setVariable(WORKFLOW_STATUS, "cancelled")
                    .correlate();
            log.info("Message cancelled sent to process with businessKey {}", businessKey);

        } else {
            log.info("No process is waiting event_cancelled message for{} ", businessKey);
        }
    }

    @PostMapping("/signal/cancel/{businessKey}")
    public void correlateCancelSignal(@PathVariable("businessKey") String businessKey) {

        List<Execution> executionQueryList = runtimeService.createExecutionQuery()
                .processInstanceBusinessKey(businessKey)  // Filter by process instance ID
                .signalEventSubscriptionName(MessageConstant.LOAN_APPLICATION_CANCELLED)  // Check if waiting for this message
                .unlimitedList();

        if (!CollectionUtils.isEmpty(executionQueryList)) {
            for (Execution execution : executionQueryList) {
//                Execution executionStillActive = runtimeService.createExecutionQuery().executionId(execution.getId()).singleResult();
//                if(executionStillActive != null) { //TODO uncomment for demo
                    runtimeService.signalEventReceived(MessageConstant.LOAN_APPLICATION_CANCELLED, execution.getId(),
                            Variables.createVariables()
                                    .putValue(DemoConstant.TEST_PURPOSE, "Signal Cancelled Received"));
                    log.info("Signal cancelled sent to process {} with businessKey {}", execution.getId(), businessKey);
  //              }
//                else {
//                    log.info("Process with execution ID {} not found", execution.getId());
//                }
            }
        } else {
            log.info("No signal waiting with business key {} not found",businessKey );
        }

    }
}
