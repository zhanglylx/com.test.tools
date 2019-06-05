package SquirrelFrame;

import ZLYUtils.JavaUtils;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class SquirrelConfig {
    public static final String guanyu = "v4.0.5 更新内容\n  1.添加广告从接口方式改为数据库方式  \n  2.解决录屏与截图模块图片卡死现象，因为没有导入lang3的包";
    public static final String TOOLSTITLE = "松鼠";
    public static final String OPENWAIT = "文件较大，请等待";
    public static final String Screenshot_save_path = "image" + File.separator + "screen" + File.separator; //录屏与截图保存地址
    public static final String FFMPEGPATH = "lib" + File.separator + "ffmpeg" + File.separator + "bin" + File.separator + "ffmpeg.exe";
    public static final String UI = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
    public static final String FILE_UI = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    public static final String DEFAULT_PATH = JavaUtils.getLocalDesktopPath().getPath();


}
