package Squirrel;

import javax.swing.*;

public class LeaveBug extends JDialog{
    public LeaveBug(String title,JDialog jdialog){
        super(jdialog,true);
        setTitle(title);

    }

    public static void main(String[] args) {
        new LeaveBug("",new JDialog());
    }
}
