package SquirrelFrame;

import Squirrel.InstallPackage;
import ZLYUtils.AdbUtils;
import ZLYUtils.FrameUtils;
import ZLYUtils.JavaUtils;
import ZLYUtils.WindosUtils;
import com.worm.StratWorm;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * 首页
 */
public class HomePage extends JFrame {
    //选择的项目
    private String projectName;
    private String clearProjectName;
    public static final String ZWSC = "中文书城";
    public static final String CXB = "免费电子书";
    public static final String ZHIBO = "直播";
    public static final String clearIphone = "清理手机环境";
    public static final String installPackage = "安装apk";
    public static final String worm = "贪食蛇";
    public static final String getLocalIP = "IP Address ";
    public static final String workFlow = "帮助文档";
    public static final String testTools = "测试工具";
    public static final String VIDEOSWICTH = "视频压缩";
    private JButton clearIphoneButtpm;
    private JButton getLocalIPButton;
    private JButton installPackageButton;
    private static HomePage homePage;
    public static JTextField textArea;
    public static JButton cartoonLog;
    private JButton refresh;
    //动画
    public static JButton cartoon;

    /**
     * 设置风格
     */
    static {

        try {
//            com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
            UIManager.setLookAndFeel(SquirrelConfig.UI);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            ((Throwable) e).printStackTrace();
        }
    }

    private HomePage() {
        super(SquirrelConfig.TOOLSTITLE);
        setLayout(null);
        setSize(700, 500);
        setIconImage(
                Toolkit.getDefaultToolkit().getImage(SquirrelConfig.logoIcon)
        );
        //检查adb端口是否被占用
        Thread adbNetstat = new Thread(new Runnable() {
            @Override
            public void run() {
                AdbUtils.killNetStatAdb();
            }
        });
        adbNetstat.start();
        Menubar menubar = new Menubar();
        menubar.setSize(this.getWidth() * 10, 37);
        menubar.setLocation(this.getX(), 5);
        add(menubar);//添加菜单条
        add(panelProject());//添加项目
        add(functionOfPanel());
        add(addTestButton(testTools));
        setLocationRelativeTo(null);//设置窗体位置,中间显示

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { //设置退出监听器
                super.windowClosing(e);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                System.exit(0);
            }
        });
        add(addCartoon());
//        add(panelLogo());
//        pack();//自适应
        Thread t = new Thread(new Cartoon());
        t.start();
        setVisible(true);//设置窗口可见


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
                        ZHIBO.equals(text) ||
                        CXB.equals(text)) {
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

    /**
     * 设置按钮监听器
     *
     * @param f
     */
    private void buttonMouseListener(JButton f) {
        f.addActionListener(e -> {
            //判断是否为刷新按钮
            if (refresh.equals(f)) {
                refreshPhone();
                return;
            }
            String text = f.getText();
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
                case testTools:
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
        });
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
        cartoonLog = new JButton();
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
        JPanel p4 = new JPanel();
        JButton regardsButton = new JButton(Menubar.regards);
        p4.setLayout(new BorderLayout());
        p4.add(regardsButton, BorderLayout.EAST);
        buttonMouseListener(regardsButton);
        return p4;
    }

    /**
     * 项目面板
     *
     * @return
     */
    private JPanel panelProject() {
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(2, 1));
        JPanel Project = new JPanel();
        Project.add(new JLabel("请选择一个项目:"));
        JRadioButton zwsc = new JRadioButton("中文书城");
        JRadioButton cxb = new JRadioButton("免费电子书");
        JRadioButton zhibo = new JRadioButton("直播");
        jRadioButtonMouseListener(zwsc);
        jRadioButtonMouseListener(cxb);
        jRadioButtonMouseListener(zhibo);
        // 单选按钮组,同一个单选按钮组的互斥.
        ButtonGroup group = new ButtonGroup();
        group.add(zwsc);
        group.add(cxb);
        group.add(zhibo);
        Project.add(zwsc);
        Project.add(cxb);
        Project.add(zhibo);
        Project.setLayout(new FlowLayout(0));
        Project.add(new JLabel("当前设备名称:"));
        textArea = new JTextField(15);
        textArea.setFont(new Font("黑体", Font.BOLD, 15));
        AdbUtils.setDevices();
        setRefresh();
        Project.add(refresh);
        Project.add(textArea);
        p1.add(Project);
        JPanel clearJPanel = new JPanel();
        clearJPanel.setLayout(new FlowLayout(0));
        clearJPanel.add(new JLabel("请选择清理方式:"));
        JRadioButton clearCache = new JRadioButton(ClearIphone.CLEAR_CACHE);
        JRadioButton clearFile = new JRadioButton(ClearIphone.CLEAR_FILE);
        JRadioButton clearAll = new JRadioButton(ClearIphone.CLEAR_ALL);
        jRadioButtonMouseListener(clearCache);
        jRadioButtonMouseListener(clearFile);
        jRadioButtonMouseListener(clearAll);
        // 单选按钮组,同一个单选按钮组的互斥.
        ButtonGroup clear = new ButtonGroup();
        clear.add(clearAll);
        clearAll.setSelected(true);
        clearProjectName = clearAll.getText();
        clear.add(clearFile);
        clear.add(clearCache);
        clearJPanel.add(clearAll);
        clearJPanel.add(clearFile);
        clearJPanel.add(clearCache);
        clearIphoneButtpm = new JButton(clearIphone);
        clearIphoneButtpm.setEnabled(false);
        buttonMouseListener(clearIphoneButtpm);
        clearJPanel.add(clearIphoneButtpm);
        installPackageButton = new JButton(installPackage);
        buttonMouseListener(installPackageButton);
        clearJPanel.add(installPackageButton);
        p1.add(clearJPanel);

        //设置大小
        p1.setSize(this.getWidth() * 10, 100);
        p1.setLocation(this.getX() - 1, 50);
        p1.setBorder(new EtchedBorder(EtchedBorder.RAISED));

        return p1;
    }

    /**
     * 刷新按钮
     */
    private void setRefresh() {

        refresh = new JButton();     //添加刷新按钮
//        refresh.setBorder(BorderFactory.createRaisedBevelBorder());//设置凸起来的按钮
        refresh.setContentAreaFilled(false);//透明的设置
        ImageIcon icon1 = new ImageIcon(("image/refresh.png"));  // 设置按钮背景图像
        refresh.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮边框与边框内容之间的像素数
        refresh.setIcon(icon1);
//      refresh.setBorderPainted(false);// 不绘制边框
        refresh.setFocusable(true);  // 设置焦点控制
        buttonMouseListener(refresh);
    }

    /**
     * 工具面板
     *
     * @return
     */
    private JPanel functionOfPanel() {
        JPanel p2 = new JPanel();
        JButton wormButton = new JButton(worm);
        p2.add(wormButton);
        buttonMouseListener(wormButton);
        getLocalIPButton = new JButton(getLocalIP);
        buttonMouseListener(getLocalIPButton);
        p2.add(getLocalIPButton);//添加获取本地IP地址
        JButton work = new JButton(workFlow);
        buttonMouseListener(work);
        p2.add(work);
        JButton videoSwitch = new JButton(VIDEOSWICTH);
        buttonMouseListener(videoSwitch);
        p2.add(videoSwitch);
        p2.setLayout(new GridLayout(1, 2));
        p2.setSize(400, 45);
        p2.setLocation(this.getX() + 70, 153);
        return p2;
    }


    /**
     * 添加按钮
     *
     * @return
     */

    private JButton addTestButton(String buttonText1) {
        JButton testButton = FrameUtils.jbuttonImage("image/TestTools.png");
        testButton.setText(buttonText1);
        testButton.setFont(new Font("Arial", Font.BOLD, 0));
        testButton.setLocation(this.getX() - 10, 150);
        testButton.setSize(100, 50);
        buttonMouseListener(testButton);
        return testButton;
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
                "中文万维", "测试牛掰"};
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
