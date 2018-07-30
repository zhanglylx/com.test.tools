package SquirrelFrame;

import javax.swing.*;
import java.awt.*;

/**
 * 文本输出框
 */
public class OutputText {
    private JTextArea logPaint = null;
    private JScrollPane jsc = null;
    public OutputText(){
        logPaint = new JTextArea();
        logPaint.setLineWrap(true);
        logPaint.setWrapStyleWord(true);
        logPaint.setEditable(false);
        jsc = new JScrollPane(logPaint);
        logPaint.setFont(new Font("标楷体", Font.BOLD, 20));

    }
    public void setText(String text){
        if(!text.endsWith("\n"))text+="\n";
        this.logPaint.setText(text);
    }
    public void addText(String text){
        if(!text.endsWith("\n"))text+="\n";
        this.logPaint.append(text);
        //下面的代码就是移动到文本域的最后面
        logPaint.selectAll();
        if (logPaint.getSelectedText() != null) {
            logPaint.setCaretPosition(logPaint.getSelectedText().length());
            logPaint.requestFocus();
        }
    }

    /**
     * 添加文本
     * @param text
     * @param line  是否自动加入换行符
     */
    public void addText(String text,boolean line){
        if(line){
            addText(text);
            return;
        }
        this.logPaint.append(text);
        //下面的代码就是移动到文本域的最后面
        logPaint.selectAll();
        if (logPaint.getSelectedText() != null) {
            logPaint.setCaretPosition(logPaint.getSelectedText().length());
            logPaint.requestFocus();
        }
    }


    public JScrollPane getJsc() {
        return jsc;
    }
}
