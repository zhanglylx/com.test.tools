package ZLYUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static ZLYUtils.AdbUtils.adbBufferedReader;
import static ZLYUtils.AdbUtils.close;

public class AaptUtils {
    public static String[] runAapt(String code) {
        String[] str = null;
        Process pro = null;
        BufferedReader br = null;
        try {
            pro = Runtime.getRuntime().exec("platform-tools" + File.separator + "aapt.exe " + code);
            br = new BufferedReader(new InputStreamReader(pro.getInputStream(), Charset.forName("utf-8")));
            str = adbBufferedReader(br);
            if (str == null) str = errRunAapt(code);
            pro.waitFor();
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{e.toString()};
        } finally {
            close(br, pro);
        }
        return str;
    }

    /**
     * 获取错误
     *
     * @param code
     * @return
     */
    private static String[] errRunAapt(String code) {
        String[] str = null;
        Process pro = null;
        BufferedReader br = null;
        try {
            pro = Runtime.getRuntime().exec("platform-tools" + File.separator + "aapt.exe " + code);
            br = new BufferedReader(new InputStreamReader(pro.getErrorStream(), Charset.forName("utf-8")));
            str = adbBufferedReader(br);
            pro.waitFor();
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(br, pro);
        }
        return str;
    }
}
