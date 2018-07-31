package InterfaceTesting;

import SquirrelFrame.SquirrelConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 松鼠工具接口测试页面
 */
public class SquirrelUi extends JDialog {
    public SquirrelUi(JDialog jDialog) {
        super(jDialog, true);
        setTitle("接口测试工具");
        setLayout(null);
        setSize(750, 500);
        add(new Case(this));
        setVisible(true);

    }

    public static void main(String[] args) {
        new SquirrelUi(new JDialog());
    }


}

class Case extends JPanel {
    private JTextArea path;//路径
    private JTextArea urlArguments;//get参数
    private JTextArea bodyArguments;//get参数
    private JRadioButton agreement;//协议
    private ButtonGroup group;//唯一按钮点击限制
    private JDialog jDialog; //父面板
    private JRadioButton method;//方法
    private JButton submit;//提交按钮
    private JButton saveCase;//保存用例按钮

    public Case(JDialog jDialog) {
        this.jDialog = jDialog;
        setLayout(null);
        setAgreementAndMethod("http");
        setAgreementAndMethod("https");
        setJTextArea();//设置路径
        setAgreementAndMethod("get");
        setAgreementAndMethod("post");
        setSubmit();//设置提交按钮
        setSaveCase();//设置保存用例按钮
        setParameter();//设置参数
        setSize(jDialog.getWidth(), jDialog.getHeight());
        setVisible(true);
    }

    /**
     * 设置路径
     */
    private void setJTextArea() {
        setText("路径:", 100, 100, 5, 25);
        this.path = setJTextArea(this.path);
        add(setJScrollPane(this.path, this.jDialog.getWidth() - 80,
                30, 50, getY() + 63));

    }

    /**
     * 设置协议与方法
     *
     * @param title
     */
    private void setAgreementAndMethod(String title) {
        if ("http".equals(title)) {
            setText("协议:", 100, 100, 5, -20);
            group = new ButtonGroup();
            agreement = new JRadioButton(title);
            agreement.setSize(50, 20);
            agreement.setLocation(40, 20);
            jRadioButtonMouseListener(agreement);
            add(agreement);
            group.add(agreement);
        } else if ("https".equals(title)) {
            agreement = new JRadioButton(title);
            agreement.setSize(60, 20);
            agreement.setLocation(90, 20);
            jRadioButtonMouseListener(agreement);
            add(agreement);
            group.add(agreement);
        } else if ("get".equals(title)) {
            group = new ButtonGroup();
            setText("方法:", 50, 50, 250, 5);
            method = new JRadioButton(title);
            method.setSize(45, 20);
            method.setLocation(285, 22);
            jRadioButtonMouseListener(method);
            add(method);
            group.add(method);
        } else if ("post".equals(title)) {
            method = new JRadioButton(title);
            method.setSize(70, 20);
            method.setLocation(330, 22);
            jRadioButtonMouseListener(method);
            add(method);
            group.add(method);
        }
    }

    /**
     * 设置JRadioButton监听器
     *
     * @param f
     */
    private void jRadioButtonMouseListener(JRadioButton f) {
        f.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = f.getText();
                if ("http".equals(text)) {
                    System.out.println(text);
                }
            }
        });
    }

    /**
     * 设置按钮监听器
     *
     * @param f
     */
    private void jButtonMouseListener(JButton f) {
        f.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = f.getText();
                if ("http".equals(text)) {
                    System.out.println(text);
                }
            }
        });
    }

    /**
     * 设置提交按钮
     */
    private void setSubmit() {
        this.submit = setJButton("提交", 60, 40, 400, 12);
        add(this.submit);
    }

    /**
     * 设置保存用例按钮
     */
    private void setSaveCase(){
        this.saveCase = setJButton("保存用例", 90, 40, 500, 12);
        add(this.saveCase);
    }

    /**
     * 设置参数模板
     */
    private void setParameter() {
        JTabbedPane sele = new JTabbedPane();
        sele.addTab("Parameter", setParameters());
        sele.addTab("sdf", new JPanel());
        sele.setSize(this.jDialog.getWidth() - 35, 550);
        sele.setLocation(10, 100);
        add(sele);


    }

    /**
     * 设置http中的参数模板
     */
    private JPanel setParameters() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        String title = "同请求一起发送的参数";
        jPanel.add(setJLbael(title, 200, 100,
                this.jDialog.getWidth() / 2 - title.length() - 55, -30));
        jPanel.add(setJLbael("url中的参数:", 100, 100, 5, 15));
        this.urlArguments = setJTextArea(this.urlArguments);
        jPanel.add(setJScrollPane(this.urlArguments, 600, 60, 100, 40));
        jPanel.add(setJLbael("body中的参数:", 100, 100, 5, 176));
        this.bodyArguments = setJTextArea(this.bodyArguments);
        jPanel.add(setJScrollPane(this.bodyArguments, 600, 170, 100, 150));
        return jPanel;
    }

    /**
     * 设置按钮
     */
    private JButton setJButton(String textTitle, int width, int height,
                               int x, int y) {
        JButton jbutton = new JButton(textTitle);
        jbutton.setSize(width, height);
        jbutton.setLocation(x, y);
        jButtonMouseListener(jbutton);
        return jbutton;
    }


    /**
     * 设置提示文本
     */
    private void setText(String text, int width, int height, int x, int y) {
        JLabel jLabel = new JLabel(text);
        jLabel.setLocation(x, y);
        jLabel.setSize(width, height);
        add(jLabel);
    }

    /**
     * 设置JLableUI
     *
     * @param text
     * @param width
     * @param height
     * @param x
     * @param y
     * @return
     */
    private JLabel setJLbael(String text, int width, int height, int x, int y) {
        JLabel jLabel = new JLabel(text);
        jLabel.setLocation(x, y);
        jLabel.setSize(width, height);
        return jLabel;

    }

    /**
     * 设置文本输入框
     *
     * @return
     */
    private JScrollPane setJScrollPane(JTextArea jTextArea, int width, int height, int x, int y) {
        if (jTextArea == null) throw new IllegalArgumentException("jTexArea参数为空");
        JScrollPane jsp = new JScrollPane(jTextArea);
        jsp.setSize(width, height);
        jsp.setLocation(x, y);
        return jsp;
    }

    /**
     * 设置文本框
     *
     * @return
     */
    private JTextArea setJTextArea(JTextArea jTextArea) {
        jTextArea = new JTextArea();
        jTextArea.setLayout(null);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setEditable(true);
        jTextArea.setFont(SquirrelConfig.typeface);
        return jTextArea;
    }

}
