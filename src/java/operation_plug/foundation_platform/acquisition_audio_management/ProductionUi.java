package operation_plug.foundation_platform.acquisition_audio_management;

import Frame.FrontPanel;
import ZLYUtils.SwingUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class ProductionUi extends FrontPanel {
    private JButton selectFile;//选择本地Excel
    private JTextField selectFileShow;
    public ProductionUi(String title) {
        super(title);
        this.selectFile = newJButton("选择本地上传文件");
        this.selectFileShow = newJTextField();
        add(this.selectFile);
        add(this.selectFileShow);
        setSize(600, 600);
        setLocationRelativeTo(null);
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
        if (this.selectFile == f) {
            SwingUtils.selectFile();
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
