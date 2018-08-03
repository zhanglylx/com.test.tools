package InterfaceTesting;

import ZLYUtils.WindosUtils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import sun.dc.pr.PRError;

import java.io.File;
import java.util.Map;

/**
 * 执行Excel中的用例
 */
public class RunExcelCase {
    private Map<Integer, Map<String, String>> caseMap;
    private SendRequest sendRequest;

    public RunExcelCase(Map<Integer, Map<String, String>> caseMap) {
        this.caseMap = caseMap;
        sendRequest = new SendRequest();
    }

    /**
     * 运行case
     */
    public void runCase() {
        String date = WindosUtils.getDate();
        for (int i = 0; i < this.caseMap.size(); i++) {
            Map<String, String> values = this.caseMap.get(i);
            sendRequest.setPath(values.get(InterfaceConfig.PATH));
            sendRequest.setBody(values.get(InterfaceConfig.BODY));
            sendRequest.setMethod(values.get(InterfaceConfig.METHOD));
            sendRequest.setUrlValues(values.get(InterfaceConfig.FORM_DATA));
            sendRequest.setAgreementValues(values.get(InterfaceConfig.AGREEMENT));
            sendRequest.setMatchingRule(values.get(InterfaceConfig.MATCHING_RULE));
            if (values.get(InterfaceConfig.ENPECTED_RESULT) == null) {
                values.put(date, "false:enpected result为空");
                continue;
            } else {
                String repose = sendRequest.sendRequest();
                if (sendRequest.getMatchingRule().equals(InterfaceConfig.EQUALS)) {
                    if (!values.get(InterfaceConfig.ENPECTED_RESULT).equals(repose)) {
                        values.put(date, "false:实际结果:" + repose);
                    } else {
                        values.put(date, "true");
                    }
                }else{
                    if (!repose.contains(values.get(InterfaceConfig.ENPECTED_RESULT))) {
                        values.put(date, "false:实际结果:" + repose);
                    } else {
                        values.put(date, "true");
                    }
                }
            }
            caseMap.put(i, values);
        }
    }


    public Map<Integer, Map<String, String>> getCaseMap() {
        return this.caseMap;
    }

}