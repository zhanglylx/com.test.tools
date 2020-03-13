package SquirrelFrame;

import ZLYUtils.JavaUtils;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class SquirrelConfig {
    public static final String guanyu = "v4.0.6 更新内容\n  test工具中加入获取apk包权限功能";
    public static final String TOOLSTITLE = "松鼠";
    public static final String OPENWAIT = "文件较大，请等待";
    public static final String Screenshot_save_path = "image" + File.separator + "screen" + File.separator; //录屏与截图保存地址
    public static final String FFMPEGPATH = "lib" + File.separator + "ffmpeg" + File.separator + "bin" + File.separator + "ffmpeg.exe";
    public static final String UI = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
    public static final String FILE_UI = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    public static final String DEFAULT_PATH = JavaUtils.getLocalDesktopPath().getPath();
    public static final String MIAN_FEI_PACKAGE = "com.mianfeia.book";
    public static final String MIAN_ZHUI_PACKAGE = "com.mianfeizs.book";
    public static final String AI_KAN_SHU_PACKAGE = "com.mfyueduqi.book";

}
