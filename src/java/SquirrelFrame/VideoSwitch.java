package SquirrelFrame;

import java.util.ArrayList;

import Frame.FrontPanel;
import ZLYUtils.WindosUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import static SquirrelFrame.SquirrelConfig.FFMPEGPATH;

/**
 * 视频转换
 */
public class VideoSwitch extends FrontPanel {
    private File file;
    private String convertedFormat;
    private JTextArea jTextArea;
    private String savePath;
    //选择文件按钮
    private JButton selectFileButton;
    private JPanel jPanel1;
    private JPanel outLogJPane;

    public static void main(String[] args) {
        new VideoSwitch();
    }

    public VideoSwitch() {
        super("视频压缩");
        this.setLayout(newGridLayout(2, 1));
        setJPnel1();
        setSize(600, 600);
        this.setLocation(100, 20);
        this.selectFileButton = newJButton("选择视频文件");

        setVisible(true);

    }

    private void setJPnel1() {
        this.jPanel1 = newJPanel();
    }

    /**
     * @param file
     * @param convertedFormat 转换后的格式
     */
    public VideoSwitch(File file, String convertedFormat) {
        super("视频压缩");
        if (file == null) throw new IllegalArgumentException("file参数为空");
        if (convertedFormat == null) throw new IllegalArgumentException("convertedFormat为空");
        if (!file.exists()) throw new IllegalArgumentException("文件不存在");
        if (!file.isFile()) throw new IllegalArgumentException("不是文件");
        convertedFormat = convertedFormat.trim().toLowerCase();
        if (!convertedFormat.equals(".mp4") && !convertedFormat.equals(".png"))
            throw new IllegalArgumentException("convertedFormat格式不正确");
        this.convertedFormat = convertedFormat;
        this.file = file;
    }

    public VideoSwitch(File file, String convertedFormat, JTextArea jTextArea) {
        super("视频压缩");
        if (file == null) throw new IllegalArgumentException("file参数为空");
        if (convertedFormat == null) throw new IllegalArgumentException("convertedFormat为空");
        if (jTextArea == null) throw new IllegalArgumentException("jTextArea为空");
        if (!file.exists()) throw new IllegalArgumentException("文件不存在");
        if (!file.isFile()) throw new IllegalArgumentException("不是文件");
        convertedFormat = convertedFormat.trim().toLowerCase();
        if (!convertedFormat.equals(".mp4") && !convertedFormat.equals(".png"))
            throw new IllegalArgumentException("convertedFormat格式不正确");
        this.convertedFormat = convertedFormat;
        this.file = file;
        this.jTextArea = jTextArea;
    }


    public VideoSwitch(File file) {
        super("视频压缩");
        if (file == null) throw new IllegalArgumentException("file参数为空");
        if (!file.exists()) throw new IllegalArgumentException("文件不存在");
        if (!file.isFile()) throw new IllegalArgumentException("不是文件");
        this.convertedFormat = ".gif";
        this.file = file;
    }

    public void start() {
        this.savePath = file.getPath() + this.convertedFormat;
        if (new File(savePath).exists()) this.savePath =
                file.getPath() + WindosUtils.getDate("yyyyMMdd_HHmmss") + this.convertedFormat;
        try {
            new Thread(new RunFfmpeg()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int setClose() {
        return 2;
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

    class RunFfmpeg implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (jTextArea != null) {
               WindosUtils.dosExecute(FFMPEGPATH + " -i " +
                        file.getPath() + " -s " +
                        "272x480 -b:v 200k " + savePath, jTextArea, false, false, true);
                if (new File(savePath).exists()) {
                    jTextArea.append("转换成功:" + savePath);
                } else {
                    jTextArea.append("转换失败");
                }
                System.out.println();
            } else {
                WindosUtils.dosExecute(FFMPEGPATH + " -i " +
                        file.getPath() + " -s " +
                        "272x480 -b:v 200k " + savePath, null, false, false, true);
            }

        }
    }
}
