package Squirrel;

import SquirrelFrame.SquirrelConfig;
import ZLYUtils.AdbUtils;
import ZLYUtils.WindosUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import static Squirrel.VideoRecordingScreenshot.SCREENSHOT_SQUIRREL;


/**
 * 录屏与截图
 */
public class VideoRecordingScreenshot extends JDialog {
    private JButton screenshot;
    private JButton recordVideo;
    public static final String SCREENSHOT = "截屏";
    public static final String SCREENSHOT_SQUIRREL = "Squirrel.png";//截图保存名称
    public RefreshTheImage refreshTheImage;
    private JButton picture;
    private Thread threadRefreshTheImage;
    public VideoRecordingScreenshot(String title, JDialog jDialog) {
        super(jDialog, false);
        setTitle(title);
        setLayout(null);
        screenshot = new JButton(SCREENSHOT);
        screenshot.setSize(60, 40);
        screenshot.setLocation(5, 0);
        buttonMouseListener(screenshot);
        add(screenshot);
        setLocationRelativeTo(null);//设置中间显示
        setSize(400, 700);
        picture = new JButton();
        picture.setSize(300, 650);  //设置大小
        picture.setLocation(82, 0);
        refreshTheImage = RefreshTheImage.getRefreshTheImage(picture);
        threadRefreshTheImage = new Thread(refreshTheImage);
        threadRefreshTheImage.start();
        add(refreshTheImage.getjButton());
        setLocation(350, 10);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                //关闭刷新线程
                refreshTheImage.stopMe();
                TestTools.setJButtonEnabled(getTitle());
                jDialog.setDefaultCloseOperation(2);

            }
        });
        setVisible(true);

    }

    /**
     * 设置鼠标监听
     *
     * @param f
     */
    public void buttonMouseListener(JButton f) {
        f.addActionListener(e -> {
            String text = f.getText();
            switch (text) {
                case SCREENSHOT:
                    refreshTheImage.suspend();
                    WindosUtils.copyFile(this, SquirrelConfig.Screenshot_save_path + SCREENSHOT_SQUIRREL);
                    threadRefreshTheImage.interrupt();
                    break;
            }
        });
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
        String[] adb;
        //adb shell screencap -p /sdcard/1.png
        while (stopMe) {
            //设置锁
            synchronized (RefreshTheImage.class) {
                try {
                    adb = AdbUtils.operationAdb("shell screencap -p /sdcard/" + SCREENSHOT_SQUIRREL);
                    System.out.println(Arrays.toString(adb));
                    adb = AdbUtils.operationAdb("pull  /sdcard/" + SCREENSHOT_SQUIRREL + " " + SquirrelConfig.Screenshot_save_path + SCREENSHOT_SQUIRREL);
                    if (!Arrays.toString(adb).contains("100%")) {
                        image = new ImageIcon("image/wait.png");
                    } else {
                        image = new ImageIcon(SquirrelConfig.Screenshot_save_path + SCREENSHOT_SQUIRREL);
                    }
                    System.out.println(Arrays.toString(adb));
                    image.setImage(image.getImage().getScaledInstance(300, 650, Image.SCALE_DEFAULT));
                    jButton.setIcon(image);
                    if (!this.suspend) {
                        try {
                            Thread.sleep(100000);
                        } catch (InterruptedException e) {
                            System.out.println("睡眠中断");
                        }
                        this.suspend = true;

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        stopMe = true;
    }
}