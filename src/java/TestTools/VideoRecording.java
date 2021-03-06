package TestTools;

import SquirrelFrame.SquirrelConfig;
import ZLYUtils.AdbUtils;
import ZLYUtils.TooltipUtil;
import ZLYUtils.WindosUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class VideoRecording {
    Thread thread;
    private static final String VIDEO_PATH = "/sdcard/Squirrel.mp4";

    public VideoRecording() {
        this.thread = new Thread(new Screenrecord());
    }

    public void start() {
        if (!AdbUtils.checkDevices()) return;
        this.thread.start();
        TooltipUtil.generalTooltip(
                "录制开始，关闭提示框后录制结束。\n视频保存地址:" +
                        VIDEO_PATH);
        this.thread.interrupt();

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        String video = AdbUtils.runAdb("pull " + VIDEO_PATH
                + " " + SquirrelConfig.DEFAULT_PATH
        ).toString();
        if (!video.contains(VIDEO_PATH + ": 1 file pulled, 0 skipped.")) {
            TooltipUtil.errTooltip("视频拉取到电脑失败");
        } else {
            TooltipUtil.generalTooltip("视频拉取成功:" +
                    SquirrelConfig.DEFAULT_PATH);
        }
    }


    /**
     * 执行录取命令
     */
    static class Screenrecord implements Runnable {

        @Override
        public void run() {
            AdbUtils.runAdb("shell screenrecord " + VIDEO_PATH, null, true, false, false);
//            Process pro = null;
//            try {
//                pro = Runtime.getRuntime()
//                        .exec(
//                                "platform-tools"
//                                        + File.separator +
//                                        "adb.exe shell screenrecord " + VIDEO_PATH);
//                pro.waitFor();
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                if (pro != null) pro.destroy();
//            }
        }
    }

}
