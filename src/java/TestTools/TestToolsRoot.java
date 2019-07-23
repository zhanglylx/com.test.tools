package TestTools;


import Frame.FrontPanel;
import InterfaceTesting.InterfaceTestUi;
import TestTools.aapt.Aapt;
import TestTools.ad_configuration.AdUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TestToolsRoot extends FrontPanel {
    private JButton getADLog;
    private JButton leaveBug;
    private JButton adTestConfig;
    private JButton videoRecordingScreenshot;
    private JButton interfaceTesting;
    private JButton urlCoding;
    private JButton aaptJButton;
    public TestToolsRoot(String title) {
        super(title,false,true);
        this.setLayout(new GridLayout(4, 2));
        this.getADLog = newJButton("获取广告日志");
        add(this.getADLog);
        this.leaveBug = newJButton("版本遗留BUG");
        add(this.leaveBug);
        this.adTestConfig = newJButton("广告配置");
        add(this.adTestConfig);
        this.videoRecordingScreenshot = newJButton("录屏与截图");
        add(this.videoRecordingScreenshot);
        this.interfaceTesting = newJButton("接口测试工具");
        add(this.interfaceTesting);
        this.urlCoding=newJButton("URL解码与编码");
        add(this.urlCoding);
        this.aaptJButton = newJButton("aapt");
        add(this.aaptJButton);
        this.setSize(400, 250);
        setVisible(true);
    }

    @Override
    public int setClose() {
        return JFrame.DISPOSE_ON_CLOSE;
    }

    @Override
    public void jRadioButtonClickEvent(JRadioButton jRadioButton) {

    }

    @Override
    public void jTextFieldEnteredEvent(JTextField jTextField) {

    }

    @Override
    public void jTextFieldReleasedEvent(JTextField jTextField) {

    }

    @Override
    public void jTextFieldExitedEvent(JTextField jTextField) {

    }

    @Override
    public void jTextFieldInputEvent(JTextField jTextField, KeyEvent e) {

    }

    @Override
    public void jTextFieldPressedEvent(JTextField jTextField) {

    }

    @Override
    public void jTextFieldClickEvent(JTextField jTextField) {

    }

    @Override
    public void buttonClickEvent(JButton jButton) {
        if (this.adTestConfig == jButton) {
            jButton.setEnabled(false);
            new AdUi(jButton.getText());
            jButton.setEnabled(true);
        } else if (this.getADLog == jButton) {
            new GetADLogs(jButton.getText(),jButton);
        } else if (this.interfaceTesting == jButton) {
            new InterfaceTestUi(jButton.getText());
        } else if (this.videoRecordingScreenshot == jButton) {
            new VideoRecordingScreenshot(jButton.getText());
        }else if(this.aaptJButton == jButton){
            new Aapt(jButton.getText());
        }
    }

    @Override
    public void jComboBoxClickEvent(JComboBox jComboBox) {

    }

    @Override
    public void jComboBoxPopupMenuCanceled(JComboBox jComboBox) {

    }

    @Override
    public void jComboBoxPopupMenuWillBecomeInvisible(JComboBox jComboBox) {

    }

    @Override
    public void jComboBoxDeselectedItem(String str) {

    }

    @Override
    public void jComboBoxSelectedItem(String str) {

    }

    @Override
    public void jComboBoxPopupMenuWillBecomeVisible(JComboBox jComboBox) {

    }
}
