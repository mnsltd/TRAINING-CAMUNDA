package mu.mns.camunda.delegateMapping;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mu.mns.camunda.constant.DemoConstant;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateVariableMapping;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class DelegateMapping implements DelegateVariableMapping {

    @Override
    public void mapInputVariables(DelegateExecution superExecution, VariableMap subVariables) {

        ObjectValue orderVariable = superExecution.getVariableTyped(DemoConstant.ORDER_VARIABLE);// getVariableTyped for ObjectValue
        FileValue contractFile = superExecution.getVariableTyped(DemoConstant.CONTRACT_FILE); // getVariableTyped for FileValue
        String status = (String) superExecution.getVariable(DemoConstant.STATUS);
        Integer quantity = (Integer) superExecution.getVariable(DemoConstant.QUANTITY);
        Boolean isApproved = (Boolean) superExecution.getVariable(DemoConstant.IS_APPROVED);
        String wkfStatus = (String) superExecution.getVariable(DemoConstant.WORKFLOW_STATUS);

        subVariables.putValueTyped(DemoConstant.ORDER_VARIABLE, orderVariable);
        subVariables.putValueTyped(DemoConstant.CONTRACT_FILE, contractFile);
        subVariables.putValue(DemoConstant.STATUS, status);
        subVariables.putValue(DemoConstant.QUANTITY, quantity);
        subVariables.putValue(DemoConstant.IS_APPROVED, isApproved);
        subVariables.putValue(DemoConstant.WORKFLOW_STATUS, wkfStatus);
        subVariables.putValue(DemoConstant.TEST_PURPOSE, "This is a test purpose");
    }

    @Override
    public void mapOutputVariables(DelegateExecution superExecution, VariableScope subInstance) {
        // Map variables from sub-process back to the parent process
        String approvalStatus = (String) subInstance.getVariable(DemoConstant.STATUS);
        superExecution.setVariable("parentApprovalStatus", approvalStatus);  // Pass back to parent
    }
}
