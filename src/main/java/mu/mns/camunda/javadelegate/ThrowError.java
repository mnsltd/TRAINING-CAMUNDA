package mu.mns.camunda.javadelegate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ThrowError implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution)  {
        String businessKey = execution.getBusinessKey();
        log.error("The process with businessKey {} is in error", businessKey);
        throw  new RuntimeException();
    }
}
