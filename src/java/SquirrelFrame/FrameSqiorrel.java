package SquirrelFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 面板框架
 * 使用者可继承此类，页面会根据添加的按钮自定义布局
 */
public abstract class FrameSqiorrel extends JDialog{
    public FrameSqiorrel(String title,JDialog jdialog){
        super(jdialog,true);
        setTitle(title);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                setDefaultCloseOperation(2);

            }
        });
    }
    public void addJButton(String [] button){
        setLayout(new GridLayout(button.length,1));
        JButton j = null;
        for(String b : button){
            add(j=new JButton(b));
        }
        pack();
        setLocationRelativeTo(null);
        addButtonMouseListener(j);
    }

    abstract public void addButtonMouseListener(JButton f);
 }
