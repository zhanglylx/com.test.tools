package SquirrelFrame;

import TestTools.TestToolsRoot;
import TestTools.InstallPackage;
import ZLYUtils.AdbUtils;
import ZLYUtils.SwingUtils;
import com.worm.StratWorm;
import operation_plug.OperationPlugUi;
import Frame.FrontPanel;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * 首页
 */
public class HomePage extends FrontPanel {
    //选择的项目
    private String projectName;
    private String clearProjectName;
    static final String ZWSC = "中文";
    static final String CXB = "免电";
    static final String MZ = "免追";
    static final String IKS = "爱看书";
    private static final String clearIphone = "清理手机环境";
    private static final String installPackage = "安装apk";
    private static final String worm = "贪食蛇";
    static final String getLocalIP = "IP Address ";
    static final String workFlow = "帮助文档";
    static final String testToolsStr = "测试工具";
    private JButton testTools;
    static final String VIDEOSWICTH = "MP4压缩";
    private final JButton URLCODE = newJButton("松鼠工具");
    private JButton OPERATION;
    private JButton clearIphoneButtpm;
    private JButton getLocalIPButton;
    private JButton installPackageButton;
    private static HomePage homePage;
    public static JTextField textArea;
    public static JButton cartoonLog;
    private JButton refresh;
    //动画
    public static JButton cartoon;

    private HomePage() {
        super(SquirrelConfig.TOOLSTITLE,false);
        setLayout(null);
        setSize(800, 500);
        //检查adb端口是否被占用
        Thread adbNetstat = new Thread(new Runnable() {
            @Override
            public void run() {
                AdbUtils.killNetStatAdb();
            }
        });
        adbNetstat.start();
        add(getMenubar());//添加菜单条
        add(panelProject());//添加项目
        add(functionOfPanel());
        add(addCartoon());
//        add(panelLogo());
        Thread t = new Thread(new Cartoon());
        t.start();
        setLocationRelativeTo(null);//设置窗体位置,中间显示
        setVisible(true);//设置窗口可见

    }

    public Menubar getMenubar() {
        Menubar menubar = new Menubar();
        menubar.setSize(this.getWidth() * 10, 37);
        menubar.setLocation(this.getX(), 5);
        return menubar;
    }

    public static HomePage getHomePage() {
        if (homePage == null) homePage = new HomePage();
        return homePage;
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
                if (ZWSC.equals(text) ||
                        MZ.equals(text) ||
                        CXB.equals(text) ||
                        IKS.equals(text)) {
                    projectName = f.getText();
                    clearIphoneButtpm.setEnabled(true);
                } else if (ClearIphone.CLEAR_ALL.equals(text) ||
                        ClearIphone.CLEAR_CACHE.equals(text) ||
                        ClearIphone.CLEAR_FILE.equals(text)) {
                    clearProjectName = text;
                }

            }
        });
    }

    @Override
    public int setClose() {
        return JFrame.EXIT_ON_CLOSE;
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
        //判断是否为刷新按钮
        if (refresh.equals(f)) {
            refreshPhone();
        } else if (this.testTools == f) {
            new TestToolsRoot(f.getText());
        } else {
            String text = f.getText();
            if (OPERATION == f) {
                new OperationPlugUi(text);
                return;
            }

            switch (text) {
                case worm:
                    new StratWorm().start();
                    break;
                case clearIphone:
                    handleClickEvents(f);
                    clearIphoneButtpm.setEnabled(false);
                    break;
                case workFlow:
                    new Pane(text);
                    break;
                case installPackage:
                    handleClickEvents(f);
                    break;
                case VIDEOSWICTH:
                    new WindowsText(text, this);
                    break;
                default:
                    new WindowsText(text, this);

            }

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

    /**
     * 刷新手机动画
     */
    private void refreshPhone() {
        textArea.setText("正在刷新");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;
                while (true) {
                    refresh.setEnabled(false);
                    AdbUtils.setDevices();
                    if (i > 4) i = 0;
                    if (!textArea.getText().contains("正在刷新")) {
                        refresh.setEnabled(true);
                        break;
                    }
                    switch (i) {
                        case 0:
                            textArea.setText("正在刷新.");
                            break;
                        case 1:
                            textArea.setText("正在刷新..");
                            break;
                        case 2:
                            textArea.setText("正在刷新...");
                            break;
                        default:
                            textArea.setText("正在刷新....");
                            break;
                    }
                    i++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    /**
     * 处理点击事件
     *
     * @param f
     */
    private void handleClickEvents(JButton f) {
        String text = f.getText();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (clearIphone.equals(text)) {
                    new ClearIphone(projectName, clearProjectName);
                    clearIphoneButtpm.setEnabled(true);
                } else if (installPackage.equals(text)) {
                    new InstallPackage(f);

                }
            }
        });
        t.start();
    }

    /**
     * logo面板
     *
     * @return
     */
    private JButton panelLogo() {
        cartoon = new JButton();
        cartoon.setBorderPainted(false);// 不绘制边框
        cartoon.setContentAreaFilled(false);//透明的设置
        cartoon.setLocation(this.getX() - 350, 250);
        cartoon.setSize(this.getWidth(), 200);
        return cartoon;
    }

    /**
     * 添加动画
     *
     * @return
     */
    private JButton addCartoon() {
        cartoonLog = newJButton();
        cartoonLog.setBorderPainted(false);// 不绘制边框
        cartoonLog.setContentAreaFilled(false);//透明的设置
        cartoonLog.setLocation(0, 250);
        cartoonLog.setLayout(null);
        cartoonLog.setSize(300, 30);
        return cartoonLog;
    }

    /**
     * 页面最底部按钮
     *
     * @return
     */
    private JPanel panelBottom() {
        JPanel p4 = newJPanel();
        JButton regardsButton = newJButton(Menubar.regards);
        p4.setLayout(new BorderLayout());
        p4.add(regardsButton, BorderLayout.EAST);
        return p4;
    }

    /**
     * 项目面板
     *
     * @return
     */
    private JPanel panelProject() {
        JPanel p1 = newJPanel();
        p1.setLayout(newGridLayout(2, 1));
        JPanel Project = newJPanel();
        Project.add(newJLabel("请选择一个项目:"));
        JRadioButton zwsc = newJRadioButton(ZWSC);
        JRadioButton cxb = newJRadioButton(CXB);
        JRadioButton mz = newJRadioButton(MZ);
        JRadioButton iks = newJRadioButton(IKS);
        jRadioButtonMouseListener(zwsc);
        jRadioButtonMouseListener(cxb);
        jRadioButtonMouseListener(mz);
        jRadioButtonMouseListener(iks);
        // 单选按钮组,同一个单选按钮组的互斥.
        SwingUtils.setButtonGroup(zwsc, cxb, mz, iks);
        Project.add(zwsc);
        Project.add(cxb);
        Project.add(mz);
        Project.add(iks);
        Project.setLayout(new FlowLayout(FlowLayout.LEFT));
        Project.add(newJLabel("           当前设备名称:"));
        textArea = newJTextField();
        textArea.setColumns(15);
        AdbUtils.setDevices();
        setRefresh();
        Project.add(refresh);
        Project.add(textArea);
        p1.add(Project);
        JPanel clearJPanel = newJPanel(false);
        clearJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        clearJPanel.add(newJLabel("请选择清理方式:"));
        JRadioButton clearCache = newJRadioButton(ClearIphone.CLEAR_CACHE);
        JRadioButton clearFile = newJRadioButton(ClearIphone.CLEAR_FILE);
        JRadioButton clearAll = newJRadioButton(ClearIphone.CLEAR_ALL);
        jRadioButtonMouseListener(clearCache);
        jRadioButtonMouseListener(clearFile);
        jRadioButtonMouseListener(clearAll);
        clearAll.setSelected(true);
        clearProjectName = clearAll.getText();
        SwingUtils.setButtonGroup(clearAll, clearFile, clearCache);
        clearJPanel.add(clearAll);
        clearJPanel.add(clearFile);
        clearJPanel.add(clearCache);
        clearIphoneButtpm = newJButton(clearIphone);
        clearIphoneButtpm.setEnabled(false);
        clearJPanel.add(clearIphoneButtpm);
        installPackageButton = newJButton(installPackage);
        clearJPanel.add(installPackageButton);

        p1.add(clearJPanel);
        //设置大小
        p1.setSize(this.getWidth() * 10, 91);
        p1.setLocation(this.getX() - 1, 40);
        p1.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        return p1;
    }

    /**
     * 刷新按钮
     */
    private void setRefresh() {

        refresh = newJButton("");     //添加刷新按钮
//        refresh.setBorder(BorderFactory.createRaisedBevelBorder());//设置凸起来的按钮
        refresh.setContentAreaFilled(false);//透明的设置
        ImageIcon icon1 = new ImageIcon(("image/refresh.png"));  // 设置按钮背景图像
        refresh.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮边框与边框内容之间的像素数
        refresh.setIcon(icon1);
//      refresh.setBorderPainted(false);// 不绘制边框
        refresh.setFocusable(true);  // 设置焦点控制
    }

    /**
     * 工具面板
     *
     * @return
     */
    private JPanel functionOfPanel() {
        JPanel p2 = newJPanel();
        this.testTools = newJButton();
        SwingUtils.setJButtonImage(this.testTools, "image/TestTools.png");
        p2.add(this.testTools);
        JButton wormButton = newJButton(worm);
        p2.add(wormButton);
        getLocalIPButton = newJButton(getLocalIP);
        p2.add(getLocalIPButton);//添加获取本地IP地址
        JButton work = newJButton(workFlow);
        p2.add(work);
        JButton videoSwitch = newJButton(VIDEOSWICTH);
        p2.add(videoSwitch);
        OPERATION = newJButton("运营插件");
        p2.add(OPERATION);
        p2.add(URLCODE);
        p2.setSize(this.getWidth() - 2, 50);
        p2.setLocation(0, 131);
        p2.setLayout(new GridLayout(1, 1));
        return p2;
    }


    public static void main(String[] args) {
        HomePage.getHomePage();
    }


}

/**
 * 动画类
 */
class Cartoon implements Runnable {

    int i = 0;
    int tab = 1;

    //        @Override
//    public void run() {
//        HomePage.cartoon.setIcon(new ImageIcon("image/RunMainPerformance.png"));
//        try {
//            Thread.sleep(2500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        File fileImage = null;
//        File fileTab = null;
//        String imagePath = "image" + File.separator + "cartoonLog" + File.separator + "tab";
//        while (true) {
//            if ((fileTab = new File(
//                    imagePath + tab)).isDirectory()
//                    ) {
//                if ((fileImage = new File(fileTab.getPath() + File.separator + i + ".png")).exists()
//                        ) {
//                    HomePage.cartoon.setIcon(new ImageIcon(fileImage.getPath()));
//                    i++;
//                } else {
//                    JavaUtils.sleep(5000l);
//                    i = 0;
//                    tab++;
//                }
//            } else {
//
//                break;
//            }
//            JavaUtils.sleep(100);
//        }
//    }
    public void run() {

        HomePage.cartoonLog.setFont(new Font("宋体", Font.BOLD, 25));//设置字体
        Color[] colors = new Color[]{Color.DARK_GRAY,
                Color.magenta, Color.orange, Color.yellow, Color.green, Color.blue,
                Color.BLUE, Color.darkGray, Color.pink, Color.cyan};
        int x = 0;
        int y = 250;
        boolean xBoolean = true;
        boolean yBoolean = true;
        HomePage.cartoonLog.setText(SquirrelConfig.TOOLSTITLE);
        String[] text = {System.getProperty("user.name"),
                "软件测试工具", "软件测试工具"};
        while (true) {
            if (xBoolean) {
                if (yBoolean) {
                    HomePage.cartoonLog.setLocation(x += 1, y += 1);
                } else {
                    HomePage.cartoonLog.setLocation(x += 1, y -= 1);
                }
            } else if (!xBoolean) {
                if (yBoolean) {
                    HomePage.cartoonLog.setLocation(x -= 1, y += 1);
                } else {
                    HomePage.cartoonLog.setLocation(x -= 1, y -= 1);
                }
            }
            if (new Random().nextInt(100) == 9) {
                HomePage.cartoonLog.setForeground(colors[i]);
                i++;
                HomePage.cartoonLog.setText(
                        text[new Random().nextInt(text.length)]);
            }
            if (i == colors.length - 1) i = 0;
            if (x > 400) {
                xBoolean = false;
            }
            if (x < 10) xBoolean = true;
            if (y < 250) yBoolean = true;
            if (y > 400) yBoolean = false;

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
