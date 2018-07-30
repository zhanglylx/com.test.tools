package SquirrelFrame;

import javax.swing.*;
import java.awt.*;

/**
 * 显示文本面板
 */
public class TextArea extends JPanel{
    private JTextArea logPaint;
    public TextArea(int width,int height){
        logPaint =  new JTextArea();
        logPaint.setLineWrap(true);
        logPaint.setWrapStyleWord(true);
        logPaint.setEditable(false);
        logPaint.setSize(width,height);
        logPaint.setFont(new Font("标楷体", Font.BOLD, 20));
        JScrollPane jsp = new JScrollPane(logPaint);
        add(logPaint);
    }
    public TextArea(){
        logPaint =  new JTextArea();
        logPaint.setLineWrap(true);
        logPaint.setWrapStyleWord(true);
        logPaint.setEditable(false);
        logPaint.setFont(new Font("标楷体", Font.BOLD, 20));
        JScrollPane jsp = new JScrollPane(logPaint);
        add(logPaint);
    }
    public void appendText(String text){logPaint.append(text);}
    public void setText(String text){logPaint.setText(text);}
}
