package mu.mns.camunda.javadelegate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class LoggerWorkflowStart implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution)  {
        String processInstanceId = execution.getProcessInstanceId();
        log.info("Process instance ID: {}", processInstanceId);
        String activityId = execution.getCurrentActivityId();
        String activityName = execution.getCurrentActivityName();
        log.info("Current activity ID: {}", activityId);
        log.info("Current activity name: {}", activityName);
        String businessKey = execution.getBusinessKey();
        log.info("Business key: {}", businessKey);
    }
}
