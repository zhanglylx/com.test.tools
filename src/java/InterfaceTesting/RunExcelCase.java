package InterfaceTesting;

import ZLYUtils.ExcelUtils;
import ZLYUtils.TooltipUtil;
import ZLYUtils.WindosUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 执行Excel中的用例
 */
public class RunExcelCase implements Runnable {
    private Map<Integer, Map<String, String>> caseMap;
    private SendRequest sendRequest;
    private JButton jButton;
    private boolean excelCaseClosed;//线程是否是关闭状态
    private boolean stopRun;
    private static RunExcelCase runExcelCase;
    private String runExcelPath;
    private RunExcelCase() {
        jButton = new JButton();
        sendRequest = new SendRequest();
        stopRun = false;
        excelCaseClosed = true;
        caseMap = new LinkedHashMap<>();

    }


    public static RunExcelCase getRunExcelCase() {
        if (runExcelCase == null) {
            runExcelCase = new RunExcelCase();
        }
        return runExcelCase;
    }

    public boolean getExcelCaseClosed() {
        return excelCaseClosed;
    }

    ;

    public void setRunExcelCaseStop() {
        if("关闭中".equals(jButton.getText()) ||
                InterfaceConfig.RUN_CASE.equals(jButton.getText()) ||
                InterfaceConfig.RUN_EXCEL_CASE_FINISH.equals(jButton.getText())
                )return;
        stopRun = true;
        this.jButton.setText("关闭中");
    }

    public void run() {
        if(runExcelPath==null || !new File(runExcelPath).exists() || !runExcelPath.endsWith(".xlsx")){
            TooltipUtil.errTooltip("请选择正确的路径和文件");
            return;
        }
        excelCaseClosed = false;//
        synchronized (this) {
            jButton.setEnabled(false);
            runCase();
            ExcelUtils.createExcelFile(new File(this.runExcelPath.substring(0,this.runExcelPath.lastIndexOf("."))+
                    WindosUtils.getDate("MM-dd HH-mm-ss")+".xlsx"), "RunMainPerformance", this.caseMap,true);
            jButton.setText(InterfaceConfig.RUN_CASE);
            jButton.setEnabled(true);
            excelCaseClosed = true;//状态置为默认
            stopRun = false;//状态置为默认
        }
    }

    /**
     * 运行case
     */
    public void runCase() {
        String date = WindosUtils.getDate();
        for (int i = 0; i < this.caseMap.size(); i++) {
            if (stopRun) break;
            jButton.setText("第"+(i + 1) + "条");
            Map<String, String> values = this.caseMap.get(i);
            sendRequest.setPath(values.get(InterfaceConfig.PATH.toLowerCase()));
            sendRequest.setBody(values.get(InterfaceConfig.BODY.toLowerCase()));
            sendRequest.setMethod(values.get(InterfaceConfig.METHOD.toLowerCase()));
            sendRequest.setUrlValues(values.get(InterfaceConfig.FORM_DATA.toLowerCase()));
            sendRequest.setAgreementValues(values.get(InterfaceConfig.AGREEMENT.toLowerCase()));
            sendRequest.setMatchingRule(values.get(InterfaceConfig.MATCHING_RULE.toLowerCase()));
            sendRequest.setTranscoding(values.get(InterfaceConfig.TRANSCODING_TEXT.toLowerCase()));
            sendRequest.setBeginTranscoding(values.get(InterfaceConfig.BEGIN_TRANSCODING.toLowerCase()));
            sendRequest.setEndTranscoding(values.get(InterfaceConfig.END_TRANSCODING.toLowerCase()));
            if (values.get(InterfaceConfig.ENPECTED_RESULT.toLowerCase()) == null) {
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
                    if (!repose.contains(values.get(InterfaceConfig.ENPECTED_RESULT.toLowerCase()))) {
                        values.put(date, "false:实际结果:" + repose);
                    } else {
                        values.put(date, "true");
                    }
                }
            }
            caseMap.put(i, values);
        }
        jButton.setText(InterfaceConfig.RUN_EXCEL_CASE_FINISH);
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

    public void setRunExcelPath(String runExcelPath) {
        this.runExcelPath = runExcelPath;
    }
}
