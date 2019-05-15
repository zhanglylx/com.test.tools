package SquirrelFrame;

import ZLYUtils.JavaUtils;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class SquirrelConfig {
    public static final String guanyu = "v4.0.4 更新内容\n  1.获取广告日志增加广告位  \n  2.页面更美光，按钮更清晰";
    public static final String TOOLSTITLE = "软件测试工具";
    public static final String OPENWAIT = "文件较大，请等待";
    public static final String Screenshot_save_path = "image" + File.separator + "screen" + File.separator; //录屏与截图保存地址
    public static final String FFMPEGPATH = "lib" + File.separator + "ffmpeg" + File.separator + "bin" + File.separator + "ffmpeg.exe";
    public static final String UI = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
    public static final String FILE_UI = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    public static final String DEFAULT_PATH = JavaUtils.getLocalDesktopPath().getPath();


}
