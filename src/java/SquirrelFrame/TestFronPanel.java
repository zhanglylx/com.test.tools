package SquirrelFrame;

import Squirrel.TestTools;
import ZLYUtils.FrameUtils;

import javax.swing.*;

public class TestFronPanel extends FrontPanel {
    public TestFronPanel(){
        JButton jButton = new JButton();

        buttonMouseListener(jButton);
        add(jButton);

        setSize(112,11);
        setVisible(true);
    }
    @Override
    public void setClose() {
    }

    @Override
    public void buttonClickEvent(JButton f) {

    }

    @Override
    public void buttonPressEvent(JButton f) {

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

    public static void main(String[] args) {
        new TestFronPanel();
    }
}
