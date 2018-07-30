package ZLYUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;

public class FrameUtils {
    /**
     * 设置Jbutton按钮中的图片
     * @param imageNamePath
     * @return
     */
    public static JButton jbuttonImage(String imageNamePath) {
        JButton jbutton = new JButton();
//        jbutton.setBorder(BorderFactory.createRaisedBevelBorder());//设置凸起来的按钮
        jbutton.setContentAreaFilled(false);//透明的设置
        ImageIcon icon1 = new ImageIcon((imageNamePath));  // 设置按钮背景图像
        jbutton.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮边框与边框内容之间的像素数
        jbutton.setIcon(icon1);
        jbutton.setBorderPainted(false);// 不绘制边框
        jbutton.setFocusable(true);  // 设置焦点控制
        jbutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//设置鼠标变为小手
        return jbutton;
    }

    /**
     * 保存文件选择框
     * @return
     */
    public static String saveFileFrame(Component parent,File fileName) throws IllegalArgumentException{
        if(!fileName.exists()){
           throw new IllegalArgumentException("fileName不存在:"+fileName);
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
            String path = fileChooser.getSelectedFile().getPath();
            path = path.substring(0,path.lastIndexOf(File.separator)+1);
            return path;
        }
        return null;
    }

    /**
     * 将file中文件名称转换成数组
     * @param files
     * @param arrays
     * @return
     */
    public static String[] addFilesShiftArrays(File[] files,String[] arrays){
        if(arrays==null)arrays=new String[0];
        for (File file : files) {
            arrays = Arrays.copyOf(arrays, arrays.length + 1);
            arrays[arrays.length-1]=file.getName();
        }
        return arrays;
    }

    /**
     * 添加JDialog窗口关闭监听器
     * @param jDialog
     */
    public static void jdialogClose(final JDialog jDialog){
        jDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                jDialog.setDefaultCloseOperation(2);

            }
        });
    }

    /**
     * 各种方法
     */
    public static void method(){
        /**
         * JButton方法
         */
        JButton jbutton = new JButton();
        jbutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//设置鼠标变为小手
        jbutton.setBorderPainted(false);// 不绘制边框
        jbutton.setContentAreaFilled(false);//透明的设置
        jbutton.setToolTipText("保存");//鼠标悬停文字
        jbutton.setFont(new Font("Arial",Font.BOLD,0));//设置字体

        //image.setImage(image.getImage().getScaledInstance(300, 500, Image.SCALE_DEFAULT));设置图片大小
    }



}
