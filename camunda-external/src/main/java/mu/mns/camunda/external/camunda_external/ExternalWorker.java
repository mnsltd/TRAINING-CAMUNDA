package mu.mns.camunda.external.camunda_external;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExternalWorker {

    @PostConstruct
    public void subscribeToExternalTask() {
        // Create an External Task client
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8066/engine-rest")  // The Camunda REST API endpoint
                .build();
        // Subscribe to the "myTopic" topic
        client.subscribe("myTopic")
                .lockDuration(1000)  // Time the task is locked for worker
                .handler((externalTask, externalTaskService) -> {
                    // Retrieve a variable from the external task
                    String inputVar = externalTask.getVariable("workflow_status");
                    log.info("External Task received variable: {} " , inputVar);
                    // Perform some business logic
                    String result = inputVar + " : The workflow has started successfully. BRAVO!";
                    // Complete the task and send back the result
                    externalTaskService.complete(externalTask,
                            Variables.putValue("outputVar", result));
                    log.info("External Task completed with result: {} " , result);
                })
                .open();
    }
}

