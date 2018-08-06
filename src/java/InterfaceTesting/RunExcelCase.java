package InterfaceTesting;

import ZLYUtils.ExcelUtils;
import ZLYUtils.WindosUtils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import sun.dc.pr.PRError;

import javax.swing.*;
import java.io.File;
import java.util.Map;

/**
 * 执行Excel中的用例
 */
public class RunExcelCase implements Runnable {
    private Map<Integer, Map<String, String>> caseMap;
    private SendRequest sendRequest;
    private JButton jButton;
    private boolean runExcelCaseStop;
    private boolean stopRun;
    private static RunExcelCase runExcelCase;

    private RunExcelCase() {
        sendRequest = new SendRequest();
        stopRun = false;
        runExcelCaseStop = true;

    }


    public static RunExcelCase getRunExcelCase() {
        if (runExcelCase == null) {
            runExcelCase = new RunExcelCase();
        }
        return runExcelCase;
    }

    public boolean getRunExcelCaseStop() {
        return runExcelCaseStop;
    }

    ;

    public void setRunExcelCaseStop() {
        if("关闭中".equals(jButton.getText()))return;
        stopRun = true;
        this.jButton.setText("关闭中");
    }

    public void run() {
        runExcelCaseStop = false;
        synchronized (this) {
            jButton.setEnabled(false);
            runCase();
            ExcelUtils.createExcelFile(new File(InterfaceConfig.RUN_EXCEL_CASE_SAVE_PATH), "test", this.caseMap);
            jButton.setText(InterfaceConfig.RUN_CASE);
            jButton.setEnabled(true);
            runExcelCaseStop = true;
            stopRun = false;
        }
    }

    /**
     * 运行case
     */
    public void runCase() {
        String date = WindosUtils.getDate();
        for (int i = 0; i < this.caseMap.size(); i++) {
            if (stopRun) break;
            jButton.setText(i + 1 + "");
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
                } else {
                    if (!repose.contains(values.get(InterfaceConfig.ENPECTED_RESULT))) {
                        values.put(date, "false:实际结果:" + repose);
                    } else {
                        values.put(date, "true");
                    }
                }
            }
            caseMap.put(i, values);
        }
        jButton.setText("完成");
    }


    public Map<Integer, Map<String, String>> getCaseMap() {
        return this.caseMap;
    }


    public void setCaseMap(Map<Integer, Map<String, String>> caseMap) {
        this.caseMap = caseMap;
    }

    public void setjButton(JButton jButton) {
        this.jButton = jButton;
    }
}
