package Squirrel;

import SquirrelFrame.SquirrelConfig;
import ZLYUtils.AdbUtils;
import ZLYUtils.FrameUtils;
import ZLYUtils.TooltipUtil;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;

/**
 * 安装apk包
 */
public class InstallPackage extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    JButton btn;
    JTextField textField;
    JButton jButton;
    JButton installPackage;

    public InstallPackage(JButton jButton) {
        this.setTitle(jButton.getText());
        setIconImage(
                Toolkit.getDefaultToolkit().getImage(SquirrelConfig.logoIcon)
        );
        FlowLayout layout = new FlowLayout();// 布局
        textField = new JTextField(30);// 文本域
        btn = new JButton("浏览");// 钮1
        installPackage = new JButton("安装");
        // 设置布局
        layout.setAlignment(FlowLayout.LEFT);// 左对齐
        this.setLayout(layout);
        this.setBounds(400, 200, 600, 70);
        this.setVisible(true);
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                setDefaultCloseOperation(2);
            }
        });
        btn.addActionListener(this);
        JLabel label = new JLabel("请选择文件：");// 标签
        this.add(label);
        this.add(textField);
        this.add(btn);
        this.add(installPackage);
        jButtonMouseListener(installPackage);
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
                if (f == installPackage) {
                    if (!checkInsatllPath()) return;
                    f.setEnabled(false);

                    Thread t = new Thread(new installPackageRun(f));
                    t.start();
                }
            }
        });
    }

    /**
     * 检查安装路径是否合法
     *
     * @return
     */
    private boolean checkInsatllPath() {
        String text = textField.getText().trim();
        if (text == null || "".equals(text)) {
            TooltipUtil.errTooltip("请选择一个apk包");
            return false;
        }
        if (!text.endsWith(".apk")) {
            TooltipUtil.errTooltip("请选择一个.apk后缀文件");
            return false;
        }
        if(!new File(text).exists()){
            TooltipUtil.errTooltip("文件不存在");
            return false;
        }
        return true;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setCurrentDirectory(fsv.getHomeDirectory());//默认桌面
        chooser.showDialog(new JLabel(), "选择");
        File file = chooser.getSelectedFile();
        textField.setText(file.getAbsoluteFile().toString());

    }
    class installPackageRun implements Runnable{
        private JButton jButton;
        public installPackageRun(JButton jButton){
            this.jButton = jButton;
        }
        @Override
        public void run() {
            synchronized (this) {
                String jButtonText = this.jButton.getText();
                this.jButton.setText("正在安装");
                String[] adb = AdbUtils.operationAdb("install -r " + textField.getText());
                if (adb == null) {
                    TooltipUtil.errTooltip("安装失败:失败原因不知道" );
                    this.jButton.setEnabled(true);
                    this.jButton.setText(jButtonText);
                    return;
                }

                boolean success = false;
                for (int i = 0; i < adb.length; i++) {
                    System.out.println(adb[i]);
                    if ("success".equals(adb[i].trim().toLowerCase())) {
                        TooltipUtil.generalTooltip("安装成功");
                        success = true;
                        break;
                    }
                }
                if (!success) {
                    if(Arrays.toString(adb).toLowerCase().contains("failed to stat")) {
                        TooltipUtil.errTooltip("文件名称不支持:" + Arrays.toString(adb));
                    }else {
                        TooltipUtil.errTooltip("安装失败:" + Arrays.toString(adb));
                    }
                }
                jButton.setEnabled(true);
                this.jButton.setText(jButtonText);
            }
        }
    }
}
