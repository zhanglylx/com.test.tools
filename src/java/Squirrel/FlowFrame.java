package Squirrel;

import SquirrelFrame.*;
import SquirrelFrame.Config;
import ZLYUtils.FrameUtils;
import ZLYUtils.WindosUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 3级页面面板
 */
public class FlowFrame extends Pane {
    JButton saveButton ;
    public FlowFrame(String buttonText, JDialog frame) {
        super(buttonText, frame);
        //添加制定流程中的按钮
        String[] flow = FrameUtils.addFilesShiftArrays(
                WindosUtils.getDirectoryFilesName(FlowConfig.fileSit+buttonText),new String[0]);
        addButton(flow);
        setWidthAndHeight(flow);
        setJDialog();
    }

    /**
     * 添加文档按钮和保存按钮
     * @param button
     */
    private void addButton(String[] button) {
        setLayout(new GridLayout(button.length,1));
        for (int i = 0; i < button.length; i++) {
            saveButton=FrameUtils.jbuttonImage("image/save.png");
            saveButton.setToolTipText("保存");//鼠标悬停文字
//            saveButton.setText((i+1)+"");
            saveButton.setText("save"+button[i]);
            saveButton.setFont(new Font("Arial",Font.BOLD,0));
            setButton(button[i]);
        }

    }
    /**
     * 设置按钮
     *
     * @param text
     */
    public void setButton(String text) {
        JButton testFlowButton = new JButton(text);
        testFlowButton.setLayout(new BorderLayout());
        testFlowButton.add(saveButton,BorderLayout.EAST);
        buttonMouseListener(testFlowButton);
        buttonMouseListener(saveButton);
        if (approvalProcess.equals(text)) testFlowButton.setEnabled(false);
        add(testFlowButton);
    }

    /**
     * 设置鼠标监听
     *
     * @param f-
     */
    public void buttonMouseListener(JButton f) {
        f.addActionListener(e -> {

            if(f.getText().startsWith("save")){
                ZLYUtils.WindosUtils.copyFile(this,FlowConfig.fileSit + getTitle() + File.separator +
                        f.getText().substring( f.getText().indexOf("save")+4, f.getText().length()),f);
            }else{
                f.setEnabled(false);
                ZLYUtils.WindosUtils.openFile(FlowConfig.fileSit + getTitle() + File.separator + f.getText());
                f.setText(Config.OPENWAIT);
            }

        });
    }
}
