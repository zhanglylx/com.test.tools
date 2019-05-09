package SquirrelFrame;

import ZLYUtils.AdbUtils;
import ZLYUtils.TooltipUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 清理手机环境
 */
public class ClearIphone {
    private String packageName;
    private String clearPackageName;
    public static final String CLEAR_CACHE = "清理缓存";
    public static final String CLEAR_FILE = "清理文件";
    public static final String CLEAR_ALL = "清理全部";

    public ClearIphone(String packageName, String clearPackageName) {
        if (packageName == null) throw new IllegalArgumentException("packageName为空");
        this.packageName = packageName;
        this.clearPackageName = clearPackageName;
        try {
            clear();
        } catch (IllegalArgumentException E) {
            E.printStackTrace();
            TooltipUtil.errTooltip("出现了一个未知错误，请重试");
            return;
        }

    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 清楚包和本地文件夹目录
     */
    public void clear() {
        String code = "";
        List<String> rm = new ArrayList<String>();
        switch (packageName) {
            case HomePage.ZWSC:
                code = "com.chineseall.singlebook";
                rm.add("sdcard/.chineseall");
                rm.add("sdcard/ChineseallReader");
                rm.add("sdcard/"+code);
                rm.add("sdcard/Android/data/com.chineseall.singlebook");
                break;
            case HomePage.CXB:
                code = "com.mianfeia.book";
                rm.add("sdcard/FreeBook/");
                rm.add("sdcard/.freebook");
                rm.add("sdcard/.cxb");
                rm.add("sdcard/.hide_freebook/");
                rm.add("sdcard/Android/data/com.mianfeia.book");
                rm.add("sdcard/"+code);
                rm.add("mnt/sdcard/com.mianfeia.book");
                break;
            case HomePage.MZ:
                code = "com.mianfeizs.book";
                rm.add("sdcard/Android/data/com.mianfeizs.book");
                rm.add("sdcard/FreeBook/");
                rm.add("sdcard/.freebook");
                rm.add("sdcard/.cxb");
                rm.add("sdcard/.hide_freebook/");
                rm.add("sdcard/"+code);
                rm.add("mnt/sdcard/com.mianfeizs.book");
                break;
            case HomePage.IKS:
                code = "com.mfyueduqi.book";
                rm.add("sdcard/Android/data/com.mfyueduqi.book");
                rm.add("sdcard/FreeBook/");
                rm.add("sdcard/.freebook");
                rm.add("sdcard/.cxb");
                rm.add("sdcard/.hide_freebook/");
                rm.add("sdcard/"+code);
                rm.add("mnt/sdcard/com.mfyueduqi.book");
                break;
            default:
                throw new IllegalArgumentException("未找到包名:" + packageName);
        }
        if (this.clearPackageName.equals(CLEAR_ALL) ||
                this.clearPackageName.equals(CLEAR_FILE)) {
            if (this.clearPackageName.equals(CLEAR_ALL)) {

                if (AdbUtils.operationAdb("uninstall " + code) == null) {
                    TooltipUtil.errTooltip("包卸载失败了，具体原因您查看下日志，然后自己删吧");
                    return;
                }
                if (checkIphoneAppPackageExist(code)) {
                    TooltipUtil.errTooltip("包卸载失败了，具体原因您查看下日志，然后自己删吧");
                    return;

                }
            }
            for (String r : rm) {
                String[] adb = AdbUtils.operationAdb("shell rm -r " + r);
                if (adb == null) return;
                if (Arrays.toString(adb).contains("Is a directory")) {
                    TooltipUtil.errTooltip("本地目录删除失败了:" + Arrays.toString(adb));
                    return;
                }
            }
        } else {
            String[] adb = AdbUtils.operationAdb("shell pm clear " + code);
            if (adb == null) return;
            if (!Arrays.toString(
                    adb).contains("Success")
            ) {
                TooltipUtil.errTooltip("应用缓存删除失败:" + Arrays.toString(adb));
                return;
            }
        }
        TooltipUtil.generalTooltip(packageName + ":完成");
    }

    /**
     * 检查手机中是否存在指定的包
     *
     * @return
     */
    public boolean checkIphoneAppPackageExist(String packageName) {
        boolean exist = false;
        String[] arr = AdbUtils.operationAdb("shell pm list package");
        if (arr == null) return false;
        for (String p : arr) {
            if (p.contains(packageName)) {
                exist = true;
                break;
            }
        }
        return exist;
    }


}
