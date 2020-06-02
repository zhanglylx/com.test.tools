package SquirrelFrame;

import ZLYUtils.AdbUtils;
import ZLYUtils.TooltipUtil;

import java.util.*;

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
                rm.add("sdcard/" + code);
                rm.add("sdcard/Android/data/com.chineseall.singlebook");
                break;
            case HomePage.CXB:
                code = SquirrelConfig.MIAN_FEI_PACKAGE;
                rm.add("sdcard/FreeBook/");
                rm.add("sdcard/.freebook");
                rm.add("sdcard/.cxb");
                rm.add("sdcard/.hide_freebook/");
                rm.add("sdcard/Android/data/com.mianfeia.book");
                rm.add("sdcard/" + code);
                rm.add("mnt/sdcard/com.mianfeia.book");
                break;
            case HomePage.MZ:
                code = SquirrelConfig.MIAN_ZHUI_PACKAGE;
                rm.add("sdcard/Android/data/com.mianfeizs.book");
                rm.add("sdcard/FreeBook/");
                rm.add("sdcard/.freebook");
                rm.add("sdcard/.cxb");
                rm.add("sdcard/.hide_freebook/");
                rm.add("sdcard/" + code);
                rm.add("mnt/sdcard/com.mianfeizs.book");
                break;
            case HomePage.IKS:
                code = SquirrelConfig.AI_KAN_SHU_PACKAGE;
                rm.add("sdcard/Android/data/com.mfyueduqi.book");
                rm.add("sdcard/FreeBook/");
                rm.add("sdcard/.freebook");
                rm.add("sdcard/.cxb");
                rm.add("sdcard/.hide_freebook/");
                rm.add("sdcard/" + code);
                rm.add("mnt/sdcard/com.mfyueduqi.book");
                break;
            default:
                throw new IllegalArgumentException("未找到包名:" + packageName);
        }
        try {
            switch (this.clearPackageName) {
                case CLEAR_FILE:
                    clearFile(rm);
                    break;
                case CLEAR_ALL:
                    clearFile(rm);
                    clearPackage(code);
                    break;
                case CLEAR_CACHE:
                    clearCache(code);
                    break;
                default:
                    TooltipUtil.errTooltip("没有找到要清理的内容，请联系管理员");
                    return;
            }
            TooltipUtil.generalTooltip(packageName + ":完成");
        } catch (Exception e) {
            e.printStackTrace();
            TooltipUtil.errTooltip(e.getLocalizedMessage());
        }

    }

    private void clearPackage(String packageName) {
        if (!AdbUtils.runAdb("uninstall " + packageName).contains("Success")) {
            TooltipUtil.errTooltip("包卸载失败了，具体原因您查看下日志，然后自己删吧");
            throw new RuntimeException("包卸载失败了，具体原因您查看下日志，然后自己删吧");
        }
        if (checkIphoneAppPackageExist(packageName)) {
            throw new RuntimeException("包卸载失败了，具体原因您查看下日志，然后自己删吧");
        }
    }

    private void clearFile(List<String> list) {
        List<String> adb;
        for (String r : list) {
            adb = AdbUtils.runAdb("shell rm -r " + r);
            if (adb.size() != 0 && !adb.toString().contains("No such file or directory")) {
                throw new RuntimeException("本地目录删除失败了:" + adb);
            }
        }
    }

    private void clearCache(String packageName) {
        List<String> adb = AdbUtils.runAdb("shell pm clear " + packageName);
        if (!adb.contains("Success")) {
            throw new RuntimeException("应用缓存删除失败:" + adb);
        }
    }


    /**
     * 检查手机中是否存在指定的包
     *
     * @return
     */
    public boolean checkIphoneAppPackageExist(String packageName) {
        boolean exist = false;
        List<String> arr = AdbUtils.operationAdb("shell pm list package");
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
