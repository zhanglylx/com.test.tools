package SquirrelFrame;


import javax.swing.*;

public class LogPaint extends JDialog {
    JTextArea j;
    public LogPaint(String title,JDialog jdialog){
        super(jdialog,true);
        j =new JTextArea();
        setTitle(title);
        add(j);
        setSize(200,300);
        setEnabled(false);
        setVisible(true);
        pack();
    }
    public void setText(String text){
        j.append(text);
    }
    public static void main(String[] args) {
    }






//     textArea.paintImmediately(textArea.getBounds());
//    或
//  textArea.paintImmediately(textArea.getX(), textArea.getY(), textArea.getWidth(), textArea.getHeight());
}