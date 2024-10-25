package mu.mns.camunda.javadelegate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class LoggerWorkflowEnd implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution)  {
        String businessKey = execution.getBusinessKey();
        log.info("The process with businessKey {} has stopped", businessKey);
    }
}
