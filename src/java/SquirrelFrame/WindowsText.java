package SquirrelFrame;

import ZLYUtils.FrameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/***
 * 窗口，弹出可编辑的窗体
 * 通过传入的buttonText判断进行相关功能的文案展示
 */
public class WindowsText extends JDialog implements ActionListener{

    JPanel jpanel;
    JButton jb1, jb2, jb3;
    JTextArea jta = null;
    JScrollPane jscrollPane;
    private String buttonText;
    //用于判断当前窗口是否关闭
    public static  boolean windowsClose = false;
    public WindowsText(String buttonText,JDialog frame){
        super(frame,true);
        this.buttonText=buttonText;
        setTitle(buttonText);
        setWindows();
    }
    public WindowsText(String buttonText,JFrame frame) {
        //用于限制点击主窗体
        super(frame,true);
        this.buttonText=buttonText;
        setTitle(buttonText);
        setWindows();
    }
    private void setWindows(){
        setLayout(new BorderLayout());
        jta = new JTextArea(10, 15);
        jta.setTabSize(4);
        jta.setFont(new Font("标楷体", Font.BOLD, 16));
        jta.setLineWrap(true);// 激活自动换行功能
        jta.setWrapStyleWord(true);// 激活断行不断字功能
        jscrollPane = new JScrollPane(jta);
        jpanel = new JPanel();
        jpanel.setLayout(new GridLayout(1, 3));
        jb1 = new JButton("复制");
        jb1.addActionListener(this);
        jb2 = new JButton("粘贴");
        jb2.addActionListener(this);
        jb3 = new JButton("剪切");
        jb3.addActionListener(this);

        jpanel.add(jb1);
        jpanel.add(jb2);
        jpanel.add(jb3);

        add(jscrollPane, BorderLayout.CENTER);
        add(jpanel, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                setDefaultCloseOperation(2);
                windowsClose = true;
            }
        });
        setLocationRelativeTo(null);
        setSize(400, 300);
        setLocation(400, 200);
        button(buttonText);
        setVisible(true);
    }
    // 覆盖接口ActionListener的方法actionPerformed
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jb1) {
            jta.copy();
        } else if (e.getSource() == jb2) {
            jta.paste();
        } else if (e.getSource() == jb3) {
            jta.cut();
        }
    }

    /**
     * 通过按钮点击后进行相应内容展示
     * @param text
     */
    public void button(String text){
        switch (text) {
            case HomePage.getLocalIP:
                jta.setText(ZLYUtils.WindosUtils.getLocalIP());
                jta.setBackground(Color.DARK_GRAY);
                jta.setForeground(Color.white);
                break;
            case Menubar.regards:
                jta.setEnabled(false);
                jta.setBackground(Color.DARK_GRAY);
                jta.setText(SquirrelConfig.guanyu);
                break;
            case HomePage.VIDEOSWICTH:
                setSize(600, 600);
                setLocation(400, 100);
                new VideoSwitch(new File(FrameUtils.selectFile()),".mp4",jta).start();
                break;
            default:
//                jta.setText(GetADLog.list.toString());
//                if (text.contains("GG-") ) {
//                    if(jscrollPane!=null)jscrollPane.repaint();
//                    this.repaint();
//                    jta.repaint();
//                    jta.setText(GetADLog.list.toString());
//                }
//                jta.setText(GetADLog.list.toString());
        }
    }
    public void setText(String text){
        jta.setText(text);
    }
    public JTextArea getJta() {
        return jta;
    }

}