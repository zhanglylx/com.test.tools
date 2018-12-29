package SquirrelFrame;

import ZLYUtils.JavaUtils;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class SquirrelConfig {
    public static final String guanyu = "v4.0.2 更新内容\n  1.修复已知bug\n" +
            "  2. 加入清理免电和爱看书相关配置功能\n  3.将广告配置环境切换到:manage-cx-qa.ikanshu.cn";
    public static final String TOOLSTITLE = "松鼠  v4.0.2";
    public static final String OPENWAIT = "文件较大，请等待";
    public static final String Screenshot_save_path = "image" + File.separator + "screen" + File.separator; //录屏与截图保存地址
    public static final Font typeface = new Font("标楷体", Font.BOLD, 15);
    public static final String logoIcon = "image/logo.png";
    public static final String FFMPEGPATH = "lib" + File.separator + "ffmpeg" + File.separator + "bin" + File.separator + "ffmpeg.exe";
    public static final String UI = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
    public static final String FILE_UI = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    public static final String DEFAULT_PATH = JavaUtils.getLocalDesktopPath().getPath();


}
