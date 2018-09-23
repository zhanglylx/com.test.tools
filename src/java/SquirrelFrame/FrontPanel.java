package SquirrelFrame;

import com.eltima.components.ui.DatePicker;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.awt.Font.PLAIN;

/**
 * 根面板
 */
public abstract class FrontPanel extends JFrame {
    public Color click_pressColor;//点击颜色
    public Color enterIntoColor;//鼠标悬停颜色颜色
    public Color defaultColor;//默认颜色
    public Color defaultFontColor;

    public FrontPanel(String title) {
        if (title == null) throw new IllegalArgumentException("title为空");
        setTitle(title);
        this.click_pressColor = Color.red;
        this.enterIntoColor = Color.CYAN;
        this.defaultColor = Color.lightGray;
        this.defaultFontColor = Color.DARK_GRAY;
    }

    public FrontPanel() {
        setTitle(SquirrelConfig.TOOLSTITLE);
    }

    /**
     * 设置关闭
     */
    private void addWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                setClose();
                setDefaultCloseOperation();

            }
        });
    }

    /**
     * 确认是否关闭程序
     *
     * @return true为关闭
     */
    public abstract void setClose();

    /**
     * 设置窗口关闭时的操作
     * 默认为JFrame.DISPOSE_ON_CLOSE隐藏当前窗口，并释放窗体占有的其他资源
     * JFrame.EXIT_ON_CLOSE 结束窗口所在的应用程序。在窗口被关闭的时候会退出JVM。
     */
    public void setDefaultCloseOperation() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * 设置字体点击颜色
     *
     * @param jButton
     */
    public void setJButtonClickColor(JButton jButton) {
        setJButtonBackground(jButton, click_pressColor);
        jButton.setForeground(Color.GREEN);
    }

    /**
     * 设置按钮监听器
     *
     * @param jButton
     */
    void buttonMouseListener(JButton jButton) {
        jButton.addActionListener(e -> {
            //设置颜色
            setJButtonClickColor(jButton);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    buttonClickEvent(jButton);
                }
            }).start();
        });
//        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//设置鼠标变为小手

        jButton.addMouseListener(new MouseListener() {
            //点击按钮
            public void mouseClicked(MouseEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    }
                }).start();

            }

            //按下按钮
            public void mousePressed(MouseEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        buttonPressEvent(jButton);
                    }
                }).start();

            }

            //鼠标释放
            public void mouseReleased(MouseEvent e) {

            }

            //进入按钮
            public void mouseEntered(MouseEvent e) {
                if (jButton.getBackground().getRGB() ==
                        defaultColor.getRGB())
                    setJButtonBackground(jButton, enterIntoColor);
            }


            //离开按钮
            public void mouseExited(MouseEvent e) {
                if (jButton.getBackground().getRGB() ==
                        enterIntoColor.getRGB())
                    setJButtonBackground(jButton, defaultColor);
            }

        });
    }

    public void setJButtonBackground(JButton jButtonBackground, Color color) {
        synchronized (this) {
            jButtonBackground.setBackground(color);
        }

    }


    public JButton newJButton(String title) {
        JButton jButton = new JButton(title);
        jButton.setBackground(Color.lightGray);
        jButton.setFont(new Font("Dialog", Font.PLAIN, 15));
        buttonMouseListener(jButton);
        return jButton;
    }

    public JPanel newJPanel() {
        JPanel jPanel = new JPanel();
//        jPanel.setBorder(
//                BorderFactory.createTitledBorder("分组框")); //设置面板边框，实现分组框的效果，此句代码为关键代码
//        jPanel.setBorder(
//                BorderFactory.createLineBorder(Color.BLACK)); //设置面板边框，实现分组框的效果，此句代码为关键代码
        jPanel.setBorder(newLineBorder());
        jPanel.setBackground(Color.WHITE);
        return jPanel;
    }

    /**
     *
     */
    public JLabel newJLabel(String title) {
        JLabel jLabel = new JLabel(title);
        jLabel.setFont(SquirrelConfig.typeface);
        jLabel.setBackground(Color.LIGHT_GRAY);
        jLabel.setSize(title.length() * 14, 30);
        return jLabel;
    }

    /**
     * 新建单行文本框
     *
     * @return
     */
    public JTextField newJTextField() {
        JTextField adsJTextField = new JTextField();
        adsJTextField.setFont(SquirrelConfig.typeface);
        adsJTextField.setBorder(newLineBorder());
        adsJTextField.setBackground(Color.white);
        return adsJTextField;
    }

    /**
     * 边框
     *
     * @return
     */
    public LineBorder newLineBorder() {
        return new LineBorder(
                new java.awt.Color(127, 157, 185), 1, true);
    }

    /**
     * 按钮点击事件
     *
     * @param f
     */
    public abstract void buttonClickEvent(JButton f);

    /**
     * 按钮按下事件
     *
     * @param f
     */
    public abstract void buttonPressEvent(JButton f);


    /**
     * 选择文件框
     */
    public static String selectFile() {
        JFileChooser chooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setCurrentDirectory(fsv.getHomeDirectory());//默认桌面
        chooser.showDialog(new JLabel(), "选择");
        File file = chooser.getSelectedFile();
        return (file.getAbsoluteFile().toString());

    }

    /**
     * 时间控件
     *
     * @return
     */
    public DatePicker getDatePicker(Date date,int x, int y, int width, int height) {
        final DatePicker datepick;
        // 格式
        String DefaultFormat = "yyyy-MM-dd HH:mm:ss";
        // 字体
        Font font = new Font("Times New Roman", Font.BOLD, 14);

        Dimension dimension = new Dimension(400, 50);

        int[] hilightDays = {1, 3, 5, 7};

        int[] disabledDays = {4, 6, 5, 9};
        datepick = new DatePicker(date, DefaultFormat, font, dimension);
        datepick.setBounds(x, y, width, height);
//        // 设置一个月份中需要高亮显示的日子
//        datepick.setHightlightdays(hilightDays, Color.red);
//        // 设置一个月份中不需要的日子，呈灰色显示
//        datepick.setDisableddays(disabledDays);
        // 设置国家
        datepick.setLocale(Locale.ENGLISH);
        // 设置时钟面板可见
        datepick.setTimePanleVisible(true);
        datepick.setFont(font);
        datepick.setBackground(Color.ORANGE);
        datepick.setForeground(Color.ORANGE);
        datepick.setBorder(newLineBorder());;
        return datepick;
    }

    /**
     * 设置ICon
     */
    public void setIconImage() {
        setIconImage(
                Toolkit.getDefaultToolkit().getImage(SquirrelConfig.logoIcon)
        );
    }

    /**
     * 下拉列表
     *
     * @param list
     * @return
     */
    public JComboBox newJComboBox(String[] list, int width) {
        JComboBox jComboBox = new JComboBox(list);
        jComboBox.setFont(SquirrelConfig.typeface);
        jComboBox.setBackground(Color.ORANGE);
        jComboBox.setSize(width, 30);
        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jComboBoxClickEvent(jComboBox);
            }
        });

        //popupMenuListener
        //列表打开或关闭监听器
        jComboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                jComboBoxPopupMenuCanceled(jComboBox);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                jComboBoxPopupMenuWillBecomeInvisible(jComboBox);
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                jComboBoxPopupMenuWillBecomeVisible(jComboBox);
            }
        });

        //选中监听器
        jComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (ItemEvent.SELECTED == arg0.getStateChange()) {
                    jComboBoxDeselectedItem(arg0.getItem().toString());
                }
                if (ItemEvent.DESELECTED == arg0.getStateChange()) {
                    jComboBoxSelectedItem(arg0.getItem().toString());
                }
            }
        });
        return jComboBox;
    }


    public JComboBox newJComboBox(List<String> list, int width) {
        return newJComboBox((String[]) list.toArray(), width);

    }

    /**
     * 列表单击事件
     *
     * @param jComboBox
     */
    public abstract void jComboBoxClickEvent(JComboBox jComboBox);

    /**
     * 下拉菜单取消
     *
     * @param jComboBox
     */
    public abstract void jComboBoxPopupMenuCanceled(JComboBox jComboBox);

    /**
     * 下拉菜单合上
     *
     * @param jComboBox
     */
    public abstract void jComboBoxPopupMenuWillBecomeInvisible(JComboBox jComboBox);

    public abstract void jComboBoxDeselectedItem(String str);

    public abstract void jComboBoxSelectedItem(String str);

    /**
     * 下拉菜单弹出
     *
     * @param jComboBox
     */
    public abstract void jComboBoxPopupMenuWillBecomeVisible(JComboBox jComboBox);


    /**
     * 设置风格
     */
    static {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            ((Throwable) e).printStackTrace();
        }
    }
}
