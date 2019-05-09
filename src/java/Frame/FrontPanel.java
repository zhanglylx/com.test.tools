package Frame;

import SquirrelFrame.SquirrelConfig;
import ZLYUtils.SwingUtils;
import com.eltima.components.ui.DatePicker;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 根面板
 */
public abstract class FrontPanel extends JFrame {
    private Color click_pressColor;//点击颜色
    private Color enterIntoColor;//鼠标悬停颜色颜色
    private Color defaultColor;//默认颜色
    private Color defaultFontColor;//默认字体颜色
    private Color click_Foreground;//点击字体颜色
    private Color backGroundColor;//页面背景颜色

    public FrontPanel(String title) {
        if (title == null) throw new IllegalArgumentException("title为空");
        setTitle(title);
        setIconImage();
        this.click_pressColor = Color.red;
        this.enterIntoColor = Color.PINK;
        this.defaultColor = Color.lightGray;
        this.defaultFontColor = Color.black;
        this.click_Foreground = Color.BLUE;
       this.backGroundColor = Color.WHITE;
        addWindowListener();
        this.getContentPane().setBackground(this.backGroundColor);
    }

    public FrontPanel(String title,boolean setResizable){
        this(title);
        this.setResizable(setResizable);
    }
    public FrontPanel(String title,boolean setResizable,Component c){
        this(title,setResizable);
        setLocationRelativeTo(c);//设置窗体位置
    }
    /**
     * 设置关闭
     */
    private void addWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                setDefaultCloseOperation(setClose());

            }
        });
    }

    /**
     * 设置窗口关闭时的操作
     * 默认为JFrame.DISPOSE_ON_CLOSE隐藏当前窗口，并释放窗体占有的其他资源
     * JFrame.EXIT_ON_CLOSE 结束窗口所在的应用程序。在窗口被关闭的时候会退出JVM。
     */
    public abstract int setClose();


    /**
     * 设置点击颜色
     *
     * @param jButton
     */
    public void setJButtonClickColor(JButton jButton) {
        setJButtonBackground(jButton, click_pressColor);
        jButton.setForeground(this.click_Foreground);
    }

    /**
     * 设置按钮监听器
     *
     * @param jButton
     */
    private void buttonMouseListener(JButton jButton) {
        jButton.addActionListener(e -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    buttonClickEvent(jButton);
                }
            }).start();
        });
        jButton.addMouseListener(new MouseListener() {
            //点击按钮
            public void mouseClicked(MouseEvent e) {

            }

            //按下按钮
            public void mousePressed(MouseEvent e) {
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


    /**
     * 设置按钮颜色
     *
     * @param jButtonBackground
     * @param color
     */
    public void setJButtonBackground(JButton jButtonBackground, Color color) {
        synchronized (this) {
            jButtonBackground.setBackground(color);
        }
    }

    public JRadioButton newJRadioButton(String title) {
        return newJRadioButton(title, 30);
    }



    /**
     * 新建单选按钮
     *
     * @param title
     * @return
     */
    public JRadioButton newJRadioButton(String title, int height) {
        JRadioButton jRadioButton = new JRadioButton(title);
        jRadioButton.setSize(title.length() * 22, height);
        jRadioButton.setFont(SquirrelConfig.typeface);
        jRadioButton.setBorder(newLineBorder());
        jRadioButton.setOpaque(false);
        jRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jRadioButtonClickEvent(jRadioButton);
                    }
                }).start();
            }
        });
        return jRadioButton;
    }

    /**
     * 单选按钮点击事件
     */
    public abstract void jRadioButtonClickEvent(JRadioButton jRadioButton);

    /**
     * 设置鼠标为小手
     *
     * @param jButton
     */
    public void setJButtonCursor(JButton jButton) {
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//设置鼠标变为小手
    }

   public JButton newJButton(){
        return newJButton(null);
   }
    public JButton newJButton(String title) {
        JButton jButton = new JButton(title);
        jButton.setBackground(Color.lightGray);
        jButton.setFont(new Font("Dialog", Font.PLAIN, 15));
        buttonMouseListener(jButton);
        setJButtonCursor(jButton);
        return jButton;
    }

    public JPanel newJPanel(boolean border) {
        JPanel jPanel = new JPanel();
//        jPanel.setBorder(
//                BorderFactory.createTitledBorder("分组框")); //设置面板边框，实现分组框的效果，此句代码为关键代码
//        jPanel.setBorder(
//                BorderFactory.createLineBorder(Color.BLACK)); //设置面板边框，实现分组框的效果，此句代码为关键代码
        if(border)jPanel.setBorder(newLineBorder());
        jPanel.setBackground(this.backGroundColor);
        return jPanel;
    }
    public JPanel newJPanel(){
        return newJPanel(true);
    }
    /**
     * 新建显示文本
     */
    public JLabel newJLabel(String title) {
        return newJLabel(title, 30);
    }

    /**
     * 新建显示文本
     */
    public JLabel newJLabel(String title, int height) {
        JLabel jLabel = new JLabel(title);
        jLabel.setFont(SquirrelConfig.typeface);
        jLabel.setBackground(Color.LIGHT_GRAY);
        jLabel.setSize(title.length() * 14, height);
        return jLabel;
    }

    /**
     * 新建单行文本框
     *
     * @return
     */
    public JTextField newJTextField() {
        JTextField jTextField = new JTextField();
        jTextField.setFont(SquirrelConfig.typeface);
        jTextField.setBorder(newLineBorder());
        jTextField.setBackground(Color.WHITE);
        jTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                jTextFieldInputEvent(jTextField, e);
            }
        });
        jTextField.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                jTextFieldClickEvent(jTextField);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                jTextFieldPressedEvent(jTextField);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                jTextFieldReleasedEvent(jTextField);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                jTextField.setBackground(Color.CYAN);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jTextFieldEnteredEvent(jTextField);
                    }
                }).start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jTextField.setBackground(Color.WHITE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jTextFieldExitedEvent(jTextField);
                    }
                }).start();
            }
        });
        return jTextField;
    }

    public abstract void jTextFieldEnteredEvent(JTextField jTextField);

    /**
     * 单行释放
     */
    public abstract void jTextFieldReleasedEvent(JTextField jTextField);


    /**
     * 单行离开事件
     *
     * @param jTextField
     */
    public abstract void jTextFieldExitedEvent(JTextField jTextField);

    /**
     * 单行输入事件
     *
     * @param jTextField
     */
    public abstract void jTextFieldInputEvent(JTextField jTextField, KeyEvent e);

    /**
     * 按下事件
     *
     * @param jTextField
     */
    public abstract void jTextFieldPressedEvent(JTextField jTextField);

    /**
     * 单行输入点击事件
     *
     * @param jTextField
     */
    public abstract void jTextFieldClickEvent(JTextField jTextField);

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
    public abstract void buttonClickEvent(JButton jButton);


    /**
     * 时间控件
     *
     * @return
     */
    public DatePicker getDatePicker(Date date, int x, int y, int width, int height) {
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
        datepick.setBorder(newLineBorder());
        ;
        return datepick;
    }

    /**
     * 设置ICon
     */
    private void setIconImage() {
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
     * 网格式布局
     *
     * @param rows 行数
     * @param cols 列数
     * @return GridLayout
     */
    public GridLayout newGridLayout(int rows, int cols) {
        return new GridLayout(rows, cols);
    }

    public Color getClick_pressColor() {
        return click_pressColor;
    }

    public Color getEnterIntoColor() {
        return enterIntoColor;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public Color getDefaultFontColor() {
        return defaultFontColor;
    }

    public Color getClick_Foreground() {
        return click_Foreground;
    }

    public Color getBackGroundColor() {
        return backGroundColor;
    }

    /**
     * 设置风格
     */
    static {
        SwingUtils.setUiDefault();
    }
}
