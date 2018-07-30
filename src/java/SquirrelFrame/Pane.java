package SquirrelFrame;

import Squirrel.FlowConfig;
import Squirrel.FlowFrame;
import Squirrel.TestTools;
import ZLYUtils.FrameUtils;
import ZLYUtils.WindosUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static Squirrel.TestTools.invokingTestFrame;
import static Squirrel.TestTools.testTools;

/**
 * 窗格类
 */
public class Pane extends JDialog {
    public static final String testFlow = "测试流程";
    public static final String approvalProcess = "审批流程";
    private static String[] flow;

    /**
     * 流程二级页面创建方法:在File中创建一个目录，系统会自动加在二级页面中，
     * 在三级页面中加入相应文件即可加入到三级页面中
     */
    static {
        flow = FrameUtils.addFilesShiftArrays(WindosUtils.getDirectoryFilesName(FlowConfig.fileSit),flow);
    }


    public Pane(String buttonText, JDialog frame) {
        super(frame, true);
        setTitle(buttonText);

    }

    public static boolean windowsClose = false;

    /**
     * 设置子窗格
     *
     * @param buttonText
     * @param frame
     */
    public Pane(String buttonText, JFrame frame) {
        super(frame, false);
        setTitle(buttonText);
        switch (buttonText) {
            case HomePage.workFlow:
                setLayout(new GridLayout(3, 3));
                for (String s : flow) {
                    setButton(s);
                }
                setWidthAndHeight(flow);
                break;
            case HomePage.testTools:
                setLayout(new GridLayout(3, 3));
                for(String s : TestTools.testTools){
                    setButton(s);
                }
                setWidthAndHeight(TestTools.testTools);
                break;
        }

        setJDialog();
    }

    /**
     * 设置鼠标监听
     *
     * @param f
     */
    public void buttonMouseListener(JButton f) {
        f.addActionListener(e -> {
            String text = f.getText();
            if(checkArraysContainText(flow,text)){
                new FlowFrame(f.getText(), this);
            }else if (checkArraysContainText(testTools,text)){
                invokingTestFrame(text,this);
            }
        });
    }
    public boolean checkArraysContainText(String[] arrays,String text){
        for(String str : arrays){
            if(str.equals(text))return true;
        }
        return false;
    }
    /**
     * 设置窗口
     */
    public void setJDialog() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windowsClose = true;
                super.windowClosing(e);
                setDefaultCloseOperation(2);

            }
        });
        setLocation(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);

    }
    /**
     * 设置大小
     */
    public void setWidthAndHeight(String[] len){
        int max = 0;
        for(String s : len){
            if(s.length()>max)max=s.length();
        }
        int height =len.length*50;
        if(len.length==1)height=100;
        if(max<10){
            setSize(max*45, height);
        }else{
            setSize(max*25, height);
        }
    }

    /**
     * 设置按钮
     *
     * @param text
     */
    public void setButton(String text) {
        JButton testFlowButton = new JButton(text);
        buttonMouseListener(testFlowButton);
        if (approvalProcess.equals(text)) testFlowButton.setEnabled(false);
        add(testFlowButton);
    }

    public void openFile(JButton j, String file) {
        j.setEnabled(false);
        ZLYUtils.WindosUtils.openFile(FlowConfig.fileSit + file);
    }
}
