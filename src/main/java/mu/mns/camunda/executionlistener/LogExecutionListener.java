package mu.mns.camunda.executionlistener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogExecutionListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        log.info("Execution listener invoked for process instance with businessKey {}"
                , delegateExecution.getBusinessKey());
        log.info("ActivityName : {}", delegateExecution.getCurrentActivityName());
    }
}
