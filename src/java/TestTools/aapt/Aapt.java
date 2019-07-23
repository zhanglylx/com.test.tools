package TestTools.aapt;

import Frame.FrontPanel;
import ZLYUtils.AaptUtils;
import ZLYUtils.SwingUtils;
import ZLYUtils.TooltipUtil;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * aaptUi工具页面
 */
public class Aapt extends FrontPanel {
    //    获取权限按钮
    private JButton permissionsJButton;
    //    功能区
    private JPanel functionalJPanel;
    //    显示内容
    private JTextArea jTextAreaDisplay;

    public Aapt(String title) {
        super(title, false, true);
        this.setLocation(100, 10);
        this.setSize(820, 700);
//        this.setLayout(new GridLayout(1,1));
        this.setLayout(null);
        this.functionalJPanel = newJPanel(true);
        this.functionalJPanel.setSize(150, this.getHeight() - 30);
        this.functionalJPanel.add(this.permissionsJButton = newJButton("getPermissions"));
        this.permissionsJButton.setToolTipText("请选择一个apk文件");
        add(this.functionalJPanel);
        this.jTextAreaDisplay = newJTextArea();
        this.jTextAreaDisplay.setLineWrap(false);
        JScrollPane jScrollPane = newJTexAreaJScrollPane(this.jTextAreaDisplay);
        jScrollPane.setSize(this.getWidth() - this.functionalJPanel.getWidth() - 7
                , this.functionalJPanel.getHeight());
        jScrollPane.setLocation(this.functionalJPanel.getX() + this.functionalJPanel.getWidth()
                , this.functionalJPanel.getY());
        add(jScrollPane);
        this.setVisible(true);
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
    public void buttonClickEvent(JButton jButton) {
        if (this.permissionsJButton == jButton) {
            executeGetPermissions();
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

    /**
     * 执行获取权限
     */
    private void executeGetPermissions() {
        try {
            String filePath = SwingUtils.selectFile(this, new String[]{".apk"});
            if (null == filePath) return;
            System.out.println(filePath);
            this.jTextAreaDisplay.setText("");
            for (String str : AaptUtils.runAapt("dunmp permissions " + filePath)) {
                this.jTextAreaDisplay.append(str + "\r\n");
                SwingUtils.setJTextAreaMoveEnd(this.jTextAreaDisplay);
            }
        } catch (Exception e) {
            TooltipUtil.errTooltip(e.toString());
        }
    }
}
