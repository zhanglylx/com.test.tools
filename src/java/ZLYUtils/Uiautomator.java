package ZLYUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * 通过adb命令执行UI操作
 */
public class Uiautomator {
    /**
     * 获取手机页面元素
     *
     * @return
     */
    public static String getElement() {
        String xmlName = System.currentTimeMillis() + ".xml";
        String[] uiautomator = AdbUtils.runAdb(
                " shell uiautomator dump /data/local/tmp/" + xmlName);
        if (uiautomator == null ||
                !Arrays.toString(uiautomator).contains(
                        "UI hierchary dumped to: /data/local/tmp/" + xmlName)) {
            return "没有获取到手机XML元素文件";
        }
        uiautomator = AdbUtils.runAdb(
                " pull /data/local/tmp/" + xmlName + " " + xmlName);
        if (uiautomator == null ||
                !Arrays.toString(uiautomator).contains(
                        "/data/local/tmp/" + xmlName + ": 1 file pulled.")) {
            return "拉取XML文件失败";
        }
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(xmlName));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuffer.append(str);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            new File(xmlName).delete();
        }
        return stringBuffer.toString();
    }

    /**
     * 点击事件
     */
    public static String inputTap(int x, int y) {
        String[] s = AdbUtils.runAdb("shell input tap " + x + " " + y);
        if (s != null) {
            return "坐标点击失败[" + x + "," + y + "]" + Arrays.toString(s);
        }
        return "success";
    }

    /**
     * 输入事件
     */
    public static String inputText(String str) {
        if (str == null) return "str为空";
        String[] s = AdbUtils.runAdb("shell input text \"" + str + "\"");
        if (s != null) {
            return "文本输入失败[" + str + "]不支持中文:" + Arrays.toString(s);
        }
        return "success";
    }

    /**
     * 启动app
     *
     * @param packageName
     * @param mainActivity
     * @return
     */
    public static String startApp(String packageName, String mainActivity) {
        if (packageName == null) return "包名为空";
        if (mainActivity == null) return "mainActivity为空";
        return Arrays.toString(AdbUtils.runAdb("shell am start " + packageName + "/" + mainActivity));
    }

    /**
     * 安装apk
     *
     * @param apk
     * @return
     */
    public static boolean installApk(File apk) {
        if (!apk.exists()) return false;
        String[] s = AdbUtils.runAdb(" install -r " + apk.getPath());
        if (s == null) return false;
        if ("succeed".equals(Arrays.toString(s).
                replace("[", "").
                replace("]", "").toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    /**
     * 点击home键
     * @return
     */
    public static boolean inputHome(){
        return inputKeyevent(3);
    }
    public static boolean inputKeyevent(int keyevent) {
        if (AdbUtils.runAdb("shell input keyevent  " + keyevent) == null) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(getElement());
    }

}

