package TestTools;

import Frame.FrontPanel;
import TestTools.VideoRecording;
import SquirrelFrame.SquirrelConfig;
import ZLYUtils.AdbUtils;
import ZLYUtils.SwingUtils;
import ZLYUtils.WindosUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;


/**
 * 录屏与截图
 */
public class VideoRecordingScreenshot extends FrontPanel {
    private JButton screenshot; //屏幕截图
    private JButton recordVideo;
    private JButton videoSwitch;
    public static final String SCREENSHOT = "截屏";
    public static final String SCREENSHOT_SQUIRREL = "Squirrel.png";//截图保存名称
    public static final String SCREENSHOT_LEADING = "copySquirrel.png";//前端展示获取的图片
    public static final String VIDEOSWITCH = "VS";
    public RefreshTheImage refreshTheImage;
    private JButton picture;
    private Thread threadRefreshTheImage;

    public VideoRecordingScreenshot(String title) {
        super(title);
        setLayout(null);
        screenshot = newJButton(SCREENSHOT);
        screenshot.setSize(60, 40);
        screenshot.setLocation(5, 0);
        add(screenshot);
        videoSwitch = newJButton(VIDEOSWITCH);
        videoSwitch.setSize(60, 40);
        videoSwitch.setLocation(5, 50);
        add(videoSwitch);
        setLocationRelativeTo(null);//设置中间显示
        setSize(400, 700);
        picture = newJButton("");
        picture.setSize(300, 650);  //设置大小
        picture.setLocation(82, 0);
        refreshTheImage = RefreshTheImage.getRefreshTheImage(picture);
        threadRefreshTheImage = new Thread(refreshTheImage);
        threadRefreshTheImage.start();
        add(refreshTheImage.getjButton());
        setLocation(350, 10);
        setVisible(true);
    }

    @Override
    public int setClose() {
        //关闭刷新线程
        refreshTheImage.stopMe();
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
        if (screenshot == f) {
            refreshTheImage.suspend();
            String saveFile = SwingUtils.saveFileFrame(this,
                    new File(SquirrelConfig.Screenshot_save_path + SCREENSHOT_SQUIRREL));
            if (saveFile != null) {
                if (!saveFile.endsWith(".png")) saveFile += ".png";
                WindosUtils.copyFile(new File(saveFile),
                        new File(SquirrelConfig.Screenshot_save_path + SCREENSHOT_LEADING));
            }
            refreshTheImage.suspendCancel();
            threadRefreshTheImage.interrupt();
        } else if (videoSwitch == f) {
            new VideoRecording().start();

        }
        setJButtonBackground(f, getDefaultColor());
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
}

/**
 * 截取图片并实时刷新
 */
class RefreshTheImage implements Runnable {
    // 用于停止线程
    private boolean stopMe;
    private ImageIcon image;
    private JButton jButton;
    private static RefreshTheImage refreshTheImage;
    private boolean suspend;//暂停

    private RefreshTheImage(JButton jButton) {
        this.jButton = jButton;
        stopMe = true;
        suspend = true;
        image = new ImageIcon("image/wait.png");
        image.setImage(image.getImage().getScaledInstance(200, 350, Image.SCALE_DEFAULT));
        jButton.setIcon(image);
    }

    public static RefreshTheImage getRefreshTheImage(JButton jButton) {
        if (refreshTheImage == null) {
            refreshTheImage = new RefreshTheImage(jButton);
        }
        return refreshTheImage;
    }

    public void suspend() {
        this.suspend = false;
    }

    public void suspendCancel() {
        this.suspend = true;
    }

    public void stopMe() {
        stopMe = false;
    }

    public ImageIcon getImage() {
        return this.image;
    }

    public JButton getjButton() {
        return this.jButton;
    }

    @Override
    public synchronized void run() {
        List<String> adb;
        //adb shell screencap -p /sdcard/1.png
        while (stopMe) {
            try {
                AdbUtils.operationAdb("shell screencap -p /sdcard/" + VideoRecordingScreenshot.SCREENSHOT_SQUIRREL);
                System.out.println("开始拉取图片");
                adb = AdbUtils.runAdb("pull  /sdcard/" + VideoRecordingScreenshot.SCREENSHOT_SQUIRREL + " " +
                        SquirrelConfig.Screenshot_save_path + VideoRecordingScreenshot.SCREENSHOT_SQUIRREL);
                System.out.println("拉取图片结束");
                System.out.println(adb);
                if (!adb.toString().contains("100%")) {
                    image = new ImageIcon("image/wait.png");
                } else {
                    if (this.suspend) {
                        WindosUtils.copyFile(SquirrelConfig.Screenshot_save_path + VideoRecordingScreenshot.SCREENSHOT_LEADING,
                                new File(SquirrelConfig.Screenshot_save_path + VideoRecordingScreenshot.SCREENSHOT_SQUIRREL));
                    }
                    image = new ImageIcon(SquirrelConfig.Screenshot_save_path + VideoRecordingScreenshot.SCREENSHOT_LEADING);
                }

                image.setImage(image.getImage().getScaledInstance(300, 650, Image.SCALE_DEFAULT));
                jButton.setIcon(image);
                if (!this.suspend) {
                    try {
                        System.out.println("进入睡眠");
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        System.out.println("睡眠中断");
                    }
                    this.suspend = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(StringUtils.leftPad("-", 100, "-"));
        }
        System.out.println("截图关闭");
        stopMe = true;
    }
}


