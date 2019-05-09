package operation_plug;

import Frame.FrontPanel;
import operation_plug.foundation_platform.acquisition_audio_management.ProductionUi;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class OperationPlugUi  extends FrontPanel {
    private JButton addProduction;
    public OperationPlugUi(String title) {
        super(title);
        setSize(300,200);
        setLocationRelativeTo(null);
        this.addProduction = newJButton("添加采集音频作品");
        add(this.addProduction);
        setVisible(true);
    }

    public static void main(String[] args) {
        new OperationPlugUi("运营插件");
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
        if(this.addProduction == f){
            new ProductionUi(f.getText());
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
