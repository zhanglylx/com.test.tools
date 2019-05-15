package SquirrelFrame;

import Squirrel.FlowConfig;
import Squirrel.FlowFrame;
import ZLYUtils.SwingUtils;
import ZLYUtils.TooltipUtil;
import ZLYUtils.WindosUtils;
import Frame.FrontPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Map;


/**
 * 窗格类
 * 工作流程和测试工具窗口
 */
public class Pane extends FrontPanel {
    public static final String testFlow = "测试流程";
    public static final String approvalProcess = "审批流程";
    private static String[] flow;


    /**
     * 流程二级页面创建方法:在File中创建一个目录，系统会自动加在二级页面中，
     * 在三级页面中加入相应文件即可加入到三级页面中
     */
    static {
        flow = SwingUtils.addFilesShiftArrays(WindosUtils.getDirectoryFilesName(FlowConfig.fileSit), flow);
    }


    /**
     * 设置子窗格
     *
     * @param buttonText
     */
    public Pane(String buttonText) {
        super(buttonText);
        switch (buttonText) {
            case HomePage.workFlow:
                setLayout(new GridLayout(3, 3));
                for (String s : flow) {
                    setButton(s);
                }
                setWidthAndHeight(flow);
                break;
        }
        setVisible(true);
    }

    @Override
    public int setClose() {
        return 2;
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
    public void buttonClickEvent(JButton f) {
        String text = f.getText();
        //帮助文档
        if (checkArraysContainText(flow, text)) {
            new FlowFrame(f.getText());
            //测试工具
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

    public boolean checkArraysContainText(String[] arrays, String text) {
        for (String str : arrays) {
            if (str.equals(text)) return true;
        }
        return false;
    }

    /**
     * 设置大小
     */
    public void setWidthAndHeight(String[] len) {
        int max = 0;
        for (String s : len) {
            if (s.length() > max) max = s.length();
        }
        int height = len.length * 50;
        if (len.length == 1) height = 100;
        setSize(600, height);
    }

    /**
     * 设置按钮
     *
     * @param text
     */
    public void setButton(String text) {
        JButton testFlowButton = newJButton(text);
        if (approvalProcess.equals(text)) testFlowButton.setEnabled(false);
        add(testFlowButton);
    }

}
