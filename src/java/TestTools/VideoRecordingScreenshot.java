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
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 录屏与截图
 */
public class VideoRecordingScreenshot extends FrontPanel {
    private JButton screenshot; //屏幕截图
    private JButton videoSwitch;
    public static final String SCREENSHOT = "截屏";
    public static final String SCREENSHOT_SQUIRREL = "Squirrel.png";//截图保存名称
    public static final String SCREENSHOT_LEADING = "copySquirrel.png";//前端展示获取的图片
    public static final String VIDEOSWITCH = "VS";
    private final JButton picture;
    private final RefreshTheImage refreshTheImage;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

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
        this.refreshTheImage = new RefreshTheImage();
        new Thread(refreshTheImage).start();
        add(picture);
        setLocation(350, 10);
        setVisible(true);
    }

    @Override
    public int setClose() {
        //关闭刷新线程
        this.refreshTheImage.stop();
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
            this.refreshTheImage.setInterrupt(true);
            String saveFile = SwingUtils.saveFileFrame(this,
                    new File(SquirrelConfig.Screenshot_save_path + SCREENSHOT_SQUIRREL));
            if (saveFile != null) {
                if (!saveFile.endsWith(".png")) saveFile += ".png";
                WindosUtils.copyFile(new File(saveFile),
                        new File(SquirrelConfig.Screenshot_save_path + SCREENSHOT_LEADING));
            }
            this.lock.lock();
            this.condition.signalAll();
            this.lock.unlock();
            this.refreshTheImage.setInterrupt(false);

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

    /**
     * 截取图片并实时刷新
     */
    private class RefreshTheImage implements Runnable {
        // 用于停止线程
        private volatile boolean interrupt;
        private volatile boolean stop;

        private RefreshTheImage() {
            interrupt = false;
            this.stop = false;
        }

        public void stop() {
            this.stop = true;
        }

        public void setInterrupt(boolean interrupt) {
            this.interrupt = interrupt;
        }


        @Override
        public synchronized void run() {
            List<String> adb;
            ImageIcon image;
            //adb shell screencap -p /sdcard/1.png
            while (!stop) {
                try {
                    AdbUtils.operationAdb("shell screencap -p /sdcard/" + VideoRecordingScreenshot.SCREENSHOT_SQUIRREL);
                    System.out.println("开始拉取图片");
                    adb = AdbUtils.runAdb("pull  /sdcard/" + VideoRecordingScreenshot.SCREENSHOT_SQUIRREL + "  " +
                            SquirrelConfig.Screenshot_save_path + VideoRecordingScreenshot.SCREENSHOT_SQUIRREL);
                    System.out.println("拉取图片结束");
                    System.out.println(adb);
                    if (!adb.toString().contains(VideoRecordingScreenshot.SCREENSHOT_SQUIRREL + ": 1 file pulled") || !adb.toString().contains("0 skipped")) {
                        image = new ImageIcon("image/wait.png");
                    } else {
                        if (!interrupt) {
                            WindosUtils.copyFile(SquirrelConfig.Screenshot_save_path + VideoRecordingScreenshot.SCREENSHOT_LEADING,
                                    new File(SquirrelConfig.Screenshot_save_path + VideoRecordingScreenshot.SCREENSHOT_SQUIRREL));
                        } else {
                            lock.lock();
                            condition.await();
                            lock.unlock();
                        }
                        image = new ImageIcon(SquirrelConfig.Screenshot_save_path + VideoRecordingScreenshot.SCREENSHOT_LEADING);
                    }
                    image.setImage(image.getImage().getScaledInstance(300, 650, Image.SCALE_DEFAULT));
                    picture.setIcon(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(StringUtils.leftPad("-", 100, "-"));
            }
            System.out.println("截图关闭");
        }
    }
}




