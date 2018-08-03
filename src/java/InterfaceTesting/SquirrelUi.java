package InterfaceTesting;

import SquirrelFrame.SquirrelConfig;
import ZLYUtils.ExcelUtils;
import ZLYUtils.Network;
import ZLYUtils.TooltipUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 松鼠工具接口测试页面
 */
public class SquirrelUi extends JDialog {
    public SquirrelUi(String title, JDialog jDialog) {
        super(jDialog, true);
        setTitle(title);
        setLayout(null);
        setSize(750, 700);
        add(new Case(this));
        setVisible(true);

    }

}

class Case extends JPanel {
    private JTextArea path;//路径
    private JTextArea urlArguments;//get参数
    private JTextArea bodyArguments;//body参数
    private JTextArea testPurpose;//测试目的
    private JRadioButton agreement;//协议
    private ButtonGroup group;//唯一按钮点击限制
    private JDialog jDialog; //父面板
    private JRadioButton method;//方法
    private JButton submit;//提交按钮
    private JButton saveCase;//保存用例按钮
    private SendRequest sendRequest;//发送请求
    private JTextArea resultRequest;//结果请求面板
    private JTextArea resultResponse;//结果面板
    private JButton runCaseExcel;//执行Excel按钮
    private JRadioButton matchingRule;//匹配规则

    public Case(JDialog jDialog) {
        this.jDialog = jDialog;
        sendRequest = new SendRequest();
        setLayout(null);
        setAgreementAndMethod(InterfaceConfig.URL_HTTP_AGREEMENT);
        setAgreementAndMethod(InterfaceConfig.URL_HTTPS_AGREEMENT);
        setJTextArea();//设置路径
        setAgreementAndMethod(InterfaceConfig.URL_GET_NAME);
        setAgreementAndMethod(InterfaceConfig.URL_POST_NAME);
        setAgreementAndMethod(InterfaceConfig.EQUALS);
        setAgreementAndMethod(InterfaceConfig.CONTAINS);
        setSubmit();//设置提交按钮
        setSaveCase();//设置保存用例按钮
        setParameter();//设置参数
        setTestPurpose();//设置测试目的
        setRunCaseExcel();//设置运行Excel按钮
        setSize(jDialog.getWidth(), jDialog.getHeight());
        setVisible(true);
    }

    /**
     * 设置路径
     */
    private void setJTextArea() {
        setText("路径:", 100, 100, 5, 25);
        this.path = setJTextArea(this.path);
        add(setJScrollPane(this.path, this.jDialog.getWidth() - 80,
                30, 50, getY() + 63));

    }

    /**
     * 设置协议与方法
     *
     * @param title
     */
    private void setAgreementAndMethod(String title) {
        if (InterfaceConfig.URL_HTTP_AGREEMENT.equals(title)) {
            setText("协议:", 100, 100, 5, -35);
            group = new ButtonGroup();
            agreement = new JRadioButton(title);
            agreement.setSize(50, 20);
            agreement.setLocation(40, 7);
            jRadioButtonMouseListener(agreement);
            add(agreement);
            group.add(agreement);
        } else if (InterfaceConfig.URL_HTTPS_AGREEMENT.equals(title)) {
            agreement = new JRadioButton(title);
            agreement.setSize(60, 20);
            agreement.setLocation(90, 7);
            jRadioButtonMouseListener(agreement);
            add(agreement);
            group.add(agreement);
        } else if (InterfaceConfig.URL_GET_NAME.equals(title)) {
            group = new ButtonGroup();
            setText("方法:", 50, 50, 5, 15);
            method = new JRadioButton(title);
            method.setSize(45, 20);
            method.setLocation(40, 33);
            jRadioButtonMouseListener(method);
            add(method);
            group.add(method);
        } else if (InterfaceConfig.URL_POST_NAME.equals(title)) {
            method = new JRadioButton(title);
            method.setSize(70, 20);
            method.setLocation(90, 33);
            jRadioButtonMouseListener(method);
            add(method);
            group.add(method);
        }
    }

    /**
     * 设置JRadioButton监听器
     *
     * @param f
     */
    private void jRadioButtonMouseListener(JRadioButton f) {
        f.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = f.getText();
                if (agreement.getText().contains(text)) {
                    sendRequest.setAgreementValues(text);
                } else if (InterfaceConfig.URL_GET_NAME.equals(text) ||
                        InterfaceConfig.URL_POST_NAME.equals(text)) {
                    sendRequest.setMethod(text);
                } else if(InterfaceConfig.EQUALS.equals(text) ||
                        InterfaceConfig.CONTAINS.equals(text)){
                    sendRequest.setMatchingRule(text);
                }
            }
        });
    }

    /**
     * 设置按钮监听器
     *
     * @param f
     */
    private void jButtonMouseListener(JButton f) {
        f.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = f.getText();
                if (f == submit) {
                    sendRequest.setPath(path.getText());
                    sendRequest.setUrlValues(urlArguments.getText());
                    sendRequest.setBody(bodyArguments.getText());
                    if (!checkValues()) return;
                    String reposen = sendRequest.sendRequest();
                    resultRequest.setText(Network.networkUrl);
                    if (InterfaceConfig.URL_POST_NAME.equals(sendRequest.getMethod()))
                        resultRequest.append("\nbody:" + sendRequest.getBody());
                    try {
                        resultResponse.setText(new String(reposen.getBytes( "GBK")));
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                        TooltipUtil.errTooltip(e1.toString());
                    }
                } else if (f == saveCase) {//保存用例
                    if(sendRequest.getMatchingRule() == null){
                        TooltipUtil.errTooltip("请选择一个匹配规则");
                        return;
                    }
                    Map<Integer, Map<String, String>> saveMap;
                    File file = new File(InterfaceConfig.SAVE_EXCEL_CASE_PATH);
                    try {
                        saveMap = ExcelUtils.getExcelXlsx(file);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                        saveMap = new LinkedHashMap<>();
                    }
                    Map<String, String> caseMap = new LinkedHashMap<>();
                    caseMap.put(InterfaceConfig.AGREEMENT, sendRequest.getAgreementValues());
                    caseMap.put(InterfaceConfig.PATH, sendRequest.getPath());
                    caseMap.put(InterfaceConfig.METHOD, sendRequest.getMethod());
                    caseMap.put(InterfaceConfig.BODY, sendRequest.getBody());
                    caseMap.put(InterfaceConfig.FORM_DATA, sendRequest.getUrlValues());
                    caseMap.put(InterfaceConfig.ENPECTED_RESULT, resultResponse.getText());
                    caseMap.put(InterfaceConfig.MATCHING_RULE, sendRequest.getMatchingRule());
                    caseMap.put(InterfaceConfig.TEST_PURPOSE, testPurpose.getText());
                    saveMap.put(saveMap.size(), caseMap);
                    ExcelUtils.createExcelFile(file, "test", saveMap);
                } else if (f == runCaseExcel) {
                    try {
                        RunExcelCase runExcelCase = new RunExcelCase(ExcelUtils.getExcelXlsx(new File(InterfaceConfig.SAVE_EXCEL_CASE_PATH)));
                        runExcelCase.runCase();
                        ExcelUtils.createExcelFile(new File(InterfaceConfig.RUN_EXCEL_CASE_SAVE_PATH), "test", runExcelCase.getCaseMap());
                    } catch (FileNotFoundException e1) {
                        TooltipUtil.errTooltip(e1.toString());
                    }
                }
            }
        });
    }

    /**
     * 检查参数
     */
    private boolean checkValues() {
        if (sendRequest.getAgreementValues() == null) {
            TooltipUtil.errTooltip("请选择一个协议");
            return false;
        }
        if (sendRequest.getMethod() == null) {
            TooltipUtil.errTooltip("请选择一个方法");
            return false;
        }
        if (sendRequest.getPath() == null || "".equals(sendRequest.getPath())) {
            TooltipUtil.errTooltip("请输入路径");
            return false;
        }

        return true;
    }


    /**
     * 设置提交按钮
     */
    private void setSubmit() {
        this.submit = setJButton(InterfaceConfig.SUBMIT, 60, 40, 180, 12);
        add(this.submit);
    }

    /**
     * 设置运行Excel按钮
     */
    private void setRunCaseExcel() {
        this.runCaseExcel = setJButton(InterfaceConfig.RUN_CASE, 85, 40, 645, 12);
        add(this.runCaseExcel);
    }

    /**
     * 设置保存用例按钮
     */
    private void setSaveCase() {
        this.saveCase = setJButton(InterfaceConfig.SAVE_CASE_JBUTTON, 60, 40, 580, 12);
        add(this.saveCase);
    }

    /**
     * 设置参数模板
     */
    private void setParameter() {
        JTabbedPane sele = new JTabbedPane();
        sele.addTab("Parameter", setParameters());
        sele.addTab("result", setResult());
        sele.setSize(this.jDialog.getWidth() - 35, 560);
        sele.setLocation(10, 100);
        add(sele);


    }

    /**
     * 设置http中的参数模板
     */
    private JPanel setParameters() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        String title = "同请求一起发送的参数";
        jPanel.add(setJLbael(title, 200, 100,
                this.jDialog.getWidth() / 2 - title.length() - 55, -30));
        jPanel.add(setJLbael("url参数:", 100, 100, 5, 70));
        this.urlArguments = setJTextArea(this.urlArguments);
        jPanel.add(setJScrollPane(this.urlArguments, 600, 160, 100, 40));
        jPanel.add(setJLbael("body参数:", 100, 100, 5, 310));
        this.bodyArguments = setJTextArea(this.bodyArguments);
        jPanel.add(setJScrollPane(this.bodyArguments, 600, 320, 100, 205));
        return jPanel;
    }

    /**
     * 设置测试目的
     */
    private void setTestPurpose() {
        add(setJLbael("测试目的", 70, 70, 350, 0));
        this.testPurpose = setJTextArea(this.testPurpose);
        add(setJScrollPane(this.testPurpose, 300, 50, 280, 10));
    }

    /**
     * 设置结果面板
     *
     * @return
     */
    private JPanel setResult() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.add(setJLbael("请求内容:", 100, 100, 5, 0));
        this.resultRequest = setJTextArea(this.resultRequest);
        jPanel.add(setJScrollPane(this.resultRequest, 600, 100, 100, 10));
        jPanel.add(setJLbael("响应内容:", 100, 100, 5, 250));
        this.resultResponse = setJTextArea(this.resultResponse);
        this.resultResponse.setFont(new Font("标楷体", Font.BOLD, 13));
        jPanel.add(setJScrollPane(this.resultResponse, 600, 400, 100, 110));
        jPanel.add(setJLbael("请求内容:", 100, 100, 5, 0));
        jPanel.add(setJLbael("匹配规则:", 100, 100, 5, 470));
        group = new ButtonGroup();
        this.matchingRule = new JRadioButton(InterfaceConfig.EQUALS);
        matchingRule.setSize(80, 20);
        matchingRule.setLocation(95, 510);
        jRadioButtonMouseListener(matchingRule);
        add(matchingRule);
        group.add(matchingRule);
        jPanel.add(this.matchingRule);
        matchingRule = new JRadioButton(InterfaceConfig.CONTAINS);
        matchingRule.setSize(100, 20);
        matchingRule.setLocation(180, 510);
        jRadioButtonMouseListener(matchingRule);
        add(matchingRule);
        group.add(matchingRule);
        jPanel.add(this.matchingRule);
        return jPanel;


    }


    /**
     * 设置按钮
     */
    private JButton setJButton(String textTitle, int width, int height,
                               int x, int y) {
        JButton jbutton = new JButton(textTitle);
        jbutton.setSize(width, height);
        jbutton.setLocation(x, y);
        jbutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButtonMouseListener(jbutton);

        return jbutton;
    }


    /**
     * 设置提示文本
     */
    private void setText(String text, int width, int height, int x, int y) {
        JLabel jLabel = new JLabel(text);
        jLabel.setLocation(x, y);
        jLabel.setSize(width, height);
        add(jLabel);
    }

    /**
     * 设置JLableUI
     *
     * @param text
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private JLabel setJLbael(String text, int width, int height, int x, int y) {
        JLabel jLabel = new JLabel(text);
        jLabel.setLocation(x, y);
        jLabel.setSize(width, height);
        jLabel.setFont(new Font("华为隶书", Font.BOLD, 15));
        return jLabel;

    }

    /**
     * 设置文本输入框
     *
     * @return
     */
    private JScrollPane setJScrollPane(JTextArea jTextArea, int width, int height, int x, int y) {
        if (jTextArea == null) throw new IllegalArgumentException("jTexArea参数为空");
        JScrollPane jsp = new JScrollPane(jTextArea);
        jsp.setSize(width, height);
        jsp.setLocation(x, y);
        return jsp;
    }

    /**
     * 设置文本框
     *
     * @return
     */
    private JTextArea setJTextArea(JTextArea jTextArea) {
        jTextArea = new JTextArea();
        jTextArea.setLayout(null);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setEditable(true);
        jTextArea.setFont(SquirrelConfig.typeface);
        return jTextArea;
    }

}
