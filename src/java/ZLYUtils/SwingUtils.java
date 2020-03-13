package ZLYUtils;

import SquirrelFrame.SquirrelConfig;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

public class SwingUtils {
    /**
     * 设置Jbutton按钮中的图片
     *
     * @param imageNamePath
     * @return
     */
    public static void setJButtonImage(JButton jButton, String imageNamePath) {
//        jbutton.setBorder(BorderFactory.createRaisedBevelBorder());//设置凸起来的按钮
        jButton.setContentAreaFilled(false);//透明的设置
        ImageIcon icon1 = new ImageIcon((imageNamePath));  // 设置按钮背景图像
        jButton.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮边框与边框内容之间的像素数
        jButton.setIcon(icon1);
        jButton.setBorderPainted(false);// 不绘制边框
        jButton.setFocusable(true);  // 设置焦点控制
        jButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//设置鼠标变为小手
    }

    /**
     * 设置默认界面UI
     */
    public static void setUiDefault() {
        try {
            UIManager.setLookAndFeel(SquirrelConfig.UI);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置文件选择UI
     */
    public static void setFileUi() {
        try {
            UIManager.setLookAndFeel(SquirrelConfig.FILE_UI);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文件选择框
     *
     * @return
     */
    public static String saveFileFrame(Component parent, File fileName) throws IllegalArgumentException {
        try {
            setFileUi();
            if (!fileName.exists()) {
                throw new IllegalArgumentException("fileName不存在:" + fileName);
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setBackground(Color.black);
            FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
            fileChooser.setDialogTitle("请选择要保存的路径");
            fileChooser.setApproveButtonText("确定");
            fileChooser.setSelectedFile(fileName);
            fileChooser.setCurrentDirectory(fsv.getHomeDirectory());//默认桌面
//        fileChooser.setAcceptAllFileFilterUsed(false);//去掉所有文件选项
            int ch = fileChooser.showDialog(parent, "保存文件");
            if (JFileChooser.APPROVE_OPTION == ch) {
//            path = path.substring(0,path.lastIndexOf(File.separator)+1);
                return fileChooser.getSelectedFile().getPath();
            }
        } finally {
            setUiDefault();
        }
        return null;
    }


    /**
     * 将file中文件名称转换成数组
     *
     * @param files
     * @param arrays
     * @return
     */
    public static String[] addFilesShiftArrays(File[] files, String[] arrays) {
        if (arrays == null) arrays = new String[0];
        if (files == null) throw new IllegalArgumentException("files为空");
        for (File file : files) {
            arrays = Arrays.copyOf(arrays, arrays.length + 1);
            arrays[arrays.length - 1] = file.getName();
        }
        return arrays;
    }

    public static boolean checkFileIsApk(String file) {
        return checkFileIsApk(new File(file));
    }

    public static boolean checkFileIsApk(File file) {
        if (file == null) {
            TooltipUtil.errTooltip("文件为空");
            return false;
        }
        if (!file.isFile()) {
            TooltipUtil.errTooltip("不是一个文件");
            return false;
        }
        if (!file.getName().endsWith(".apk")) {
            TooltipUtil.errTooltip("不是一个apk文件");
            return false;
        }
        return true;
    }

    /**
     * 选择apk文件
     *
     * @param parent
     * @return
     */
    public static File selectApkFile(Component parent) {
        return selectFile(parent, new String[]{".apk"});
    }

    /**
     * 选择文件框
     */
    public static File selectFile(Component parent, String[] filter) {
        try {
            setFileUi();
            JFileChooser chooser = new JFileChooser();
//            添加文件过滤器
            if (filter != null && filter.length > 0) {
                for (int i = 0; i < filter.length; i++) {
                    if (i == 0) {
                        chooser.setFileFilter(setFileFilter(filter[i]));
                    } else {
                        chooser.addChoosableFileFilter(setFileFilter(filter[i]));
                    }
                }
            }
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setCurrentDirectory(JavaUtils.getLocalDesktopPath());//默认桌面
            int ch = chooser.showDialog(parent, "选择文件");
            File file = chooser.getSelectedFile();
            if (null == file || JFileChooser.APPROVE_OPTION != ch) return null;
            boolean f = false;
            if (filter != null && filter.length > 0) {
                for (String fe : filter) {
                    if (file.getName().endsWith(fe.trim())) {
                        f = true;
                        break;
                    }
                }
            }
            if (!f) {
                TooltipUtil.errTooltip("您选择文件的格式不正确，请重新选择");
                return null;
            }
            return file;
        } finally {
            setUiDefault();
        }
    }

    /**
     * 设置文件过滤
     *
     * @param fileFilter
     * @return
     */
    private static FileFilter setFileFilter(String fileFilter) {
        return new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.getName().contains(fileFilter.trim())) return true;
                return false;
            }

            @Override
            public String getDescription() {
                return fileFilter.trim();
            }
        };
    }


    public static File selectFile(Component parent) {
        return selectFile(parent, null);
    }


    /**
     * 将文本域焦点设置到最后
     *
     * @param jTextAreaMoveEnd
     */
    public static void setJTextAreaMoveEnd(JTextComponent jTextAreaMoveEnd) {
        //        //下面的代码就是移动到文本域的最后面
        jTextAreaMoveEnd.selectAll();
        if (jTextAreaMoveEnd.getSelectedText() != null) {
            jTextAreaMoveEnd.setCaretPosition(jTextAreaMoveEnd.getSelectedText().length());
            jTextAreaMoveEnd.requestFocus();
        }
    }

    /**
     * 设置单行文本框的滚动条自动在最右侧
     *
     * @param jScrollPaneBarRight
     * @param jTextField
     */
    public static void setJScrollPaneBarRight(JScrollPane jScrollPaneBarRight, JTextField jTextField) {
        Point p = new Point();
        p.setLocation(jTextField.getText().length() * 100, 0);
        System.out.println(p);
        jScrollPaneBarRight.getViewport().setViewPosition(p);
    }

    /**
     * 设置表格指定列的列宽
     *
     * @param jTablePreferredWidth
     * @param column
     * @param width
     */
    public static void setJTablePreferredWidth(JTable jTablePreferredWidth, int column, int width) {
        jTablePreferredWidth.getColumnModel().getColumn(column).setPreferredWidth(width);
    }


    /**
     * 添加JDialog窗口关闭监听器
     *
     * @param jDialog
     */
    public static void jdialogClose(final JDialog jDialog) {
        jDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                jDialog.setDefaultCloseOperation(2);

            }
        });
    }

    /**
     * 设置单选按钮
     *
     * @param buttons
     */
    public static void setButtonGroup(AbstractButton... buttons) {
        ButtonGroup buttonGroup = new ButtonGroup();
        for (AbstractButton button : buttons) {
            buttonGroup.add(button);
        }
    }


    /**
     * 各种方法
     */
    public static void method() {
        /**
         * JButton方法
         */
        JButton jbutton = new JButton();
        jbutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//设置鼠标变为小手
        jbutton.setBorderPainted(false);// 不绘制边框
        jbutton.setContentAreaFilled(false);//透明的设置
        jbutton.setToolTipText("保存");//鼠标悬停文字
        jbutton.setFont(new Font("Arial", Font.BOLD, 0));//设置字体


        //   setIconImage(Toolkit.getDefaultToolkit().getImage(SquirrelConfig.logoIcon)); 设置Frame图标


        //image.setImage(image.getImage().getScaledInstance(300, 500, Image.SCALE_DEFAULT));设置图片大小

        /**
         * 设置窗体点击X按钮是否关闭
         */
//        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) { //设置退出监听器
//                if(RunExcelCase.getRunExceling()){
//                    TooltipUtil.errTooltip("自动化用例正在执行");
//                    return;}
//                System.out.println("===");
//                super.windowClosing(e);
//                setDefaultCloseOperation(2);
//                TestTools.setJButtonEnabled(getTitle());
//            }
//        });


    }

    /**
     * 设置按钮监听器
     */
    private void jButtonMouseListener(JButton jb) {
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    /**
     * 更新table数据
     *
     * @param jTableData
     * @param dataVector
     */
    public static void setJTableData(JTable jTableData, Vector[] dataVector) {
        Vector column = new Vector();
        DefaultTableModel dtm = (DefaultTableModel) jTableData.getModel();
        for (int i = 0; i < dtm.getColumnCount(); i++) {
            column.add(dtm.getColumnName(i));
        }
        Vector vector = new Vector();
        for (Vector v : dataVector) {
            vector.add(v);
        }
        dtm.setDataVector(vector, column);
        jTableData.setModel(dtm);
    }
}
