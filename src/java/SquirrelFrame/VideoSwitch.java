package SquirrelFrame;

import ZLYUtils.WindosUtils;

import javax.swing.*;
import java.io.File;

import static SquirrelFrame.SquirrelConfig.FFMPEGPATH;

/**
 * 视频转换
 */
public class VideoSwitch {
    private File file;
    private String convertedFormat;
    private JTextArea jTextArea;
    private String savePath;

    /**
     * @param file
     * @param convertedFormat 转换后的格式
     */
    public VideoSwitch(File file, String convertedFormat) {
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

    class RunFfmpeg implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (jTextArea != null) {
                WindosUtils.runLocalhostExe(FFMPEGPATH + " -i " +
                        file.getPath() + " -s " +
                        "272x480 -b:v 200k " + savePath, jTextArea);
                if (new File(savePath).exists()) {
                    jTextArea.append("转换成功:" + savePath);
                }else{
                    jTextArea.append("转换失败");
                }
            } else {
                WindosUtils.runLocalhostExe(FFMPEGPATH + " -i " +
                        file.getPath() + " -s " +
                        "272x480 -b:v 200k " + savePath);
            }

        }
    }
}
