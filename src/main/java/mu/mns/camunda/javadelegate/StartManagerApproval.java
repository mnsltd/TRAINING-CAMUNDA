package mu.mns.camunda.javadelegate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mu.mns.camunda.constant.MessageConstant;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class StartManagerApproval implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution)  {
        String businessKey = execution.getBusinessKey();
        execution.getProcessEngine().getRuntimeService().startProcessInstanceByMessage(MessageConstant.START_MANAGER_APPROVAL, businessKey);
        log.info("Message Sent to start approval manager for {} ",businessKey);
    }
}
