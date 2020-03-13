package ZLYUtils;

import SquirrelFrame.HomePage;

import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class AdbUtils {
    /**
     * 执行adb命令
     *
     * @return 获取到adb返回内容的字符数组
     */
    public static String devices;
    private static int killNetStatAdb;
    public static long errTime = 0;

    static {
        killNetStatAdb = -1;
    }

    /**
     * 获取错误
     *
     * @param code
     * @return
     */
    private static String[] errRunAdb(String code) {
        String[] str = null;
        Process pro = null;
        BufferedReader br = null;
        try {
            String dev = " ";
            if (devices != null) dev = " -s " + devices + " ";
            pro = Runtime.getRuntime().exec("platform-tools" + File.separator + "adb.exe" + dev + code);
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


    public static String[] runAdb(String code) {
        String[] str = null;
        Process pro = null;
        BufferedReader br = null;
        try {
            String dev = " ";
            if (devices != null) dev = " -s " + devices + " ";
            pro = Runtime.getRuntime().exec("platform-tools" + File.separator + "adb.exe" + dev + code);
            br = new BufferedReader(new InputStreamReader(pro.getInputStream(), Charset.forName("utf-8")));
            str = adbBufferedReader(br);
            if (str == null) str = errRunAdb(code);
            pro.waitFor();
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(br, pro);
        }
        return str;
    }

    static void close(BufferedReader bufferedReader, Process process) {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (process != null) process.destroy();
    }

    /**
     * 读取adb缓冲流
     *
     * @return
     */
    public static String[] adbBufferedReader(BufferedReader br) throws IOException {
        String msg;
        String[] str = new String[0];
        while ((msg = br.readLine()) != null) {
            str = Arrays.copyOf(str, str.length + 1);
            str[str.length - 1] = msg;
        }
        if (str.length == 0) return null;
        return str;
    }

    /**
     * 杀死占用5037端口应用
     */
    public static void killNetStatAdb() {
        try {
            String pid = "";
            if (WindosUtils.isPortUsing("127.0.0.1", 5037)) {
                int p = -1;
                if ((p = WindosUtils.selectNetstatPid(5037)) != -1) {
                    pid = WindosUtils.getPIDName(p);
                    if (pid == null || pid.toLowerCase().contains("adb.exe") || pid.toLowerCase().contains("java"))
                        return;
                }
            } else {
                return;
            }
            if (killNetStatAdb != -1) {
                TooltipUtil.errTooltip("您的系统adb端口5037被占用，连接手机功能宕机\n" +
                        "占用的程序:" + pid);
                return;
            }
            final String finalPid = pid;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    WindosUtils.closeProcess(finalPid);
                }
            });
            t.start();
            operationAdb("");
            killNetStatAdb++;
            killNetStatAdb();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        {

        }
    }


    /**
     * 查看设备相信信息
     *
     * @return
     */
    private static String[] devicesInfo() {
        String[] devicesArr = new String[0];
        for (String s : runAdb("devices -l")) {
            if ((s.toUpperCase().contains("device".toUpperCase()) && s.contains("model")) ||
                    s.contains("unauthorized") || s.contains("connecting")) {
                devicesArr = Arrays.copyOf(devicesArr, devicesArr.length + 1);
                devicesArr[devicesArr.length - 1] = s;
            }
        }
        return devicesArr;
    }

    /**
     * 查看设备相信信息
     *
     * @return
     */
    private static String[] checkdevicesInfo() {
        String[] devicesArr = new String[0];
        for (String s : runAdb("devices ")) {
            if ((s.toUpperCase().contains("device".toUpperCase()) && s.contains("model")) ||
                    s.contains("unauthorized") || s.contains("connecting")) {
                devicesArr = Arrays.copyOf(devicesArr, devicesArr.length + 1);
                devicesArr[devicesArr.length - 1] = s;
            }
        }
        return devicesArr;
    }

    /**
     * 执行adb命令
     *
     * @param code 执行命令
     * @return
     */
    public static String[] operationAdb(String code) {
        //检查是否连接设备
        if (!checkDevices()) {
            return null;
        }
        return runAdb(code);
    }


    /**
     * 检查设备
     */
    public static boolean checkDevices() {
        String[] deivcesInfo = checkdevicesInfo();
        for (int i = 0; i < 2; i++) {
            if (deivcesInfo.length == 0 && i == 1) {
                if (errTime < System.currentTimeMillis()) TooltipUtil.errTooltip("请至少将一台设备连接到电脑");
                errTime = System.currentTimeMillis() + (10 * 1000);
                System.out.println(Arrays.toString(deivcesInfo));
                return false;
            }
            deivcesInfo = devicesInfo();
        }
        if (devices == null) {
            setDevices();
        } else {
            boolean dev = false;
            for (String s : deivcesInfo) {
                if (s.contains(devices)) dev = true;
            }
            if (!dev) {
                TooltipUtil.errTooltip("当前设备:" + HomePage.textArea.getText() + " 已更换，设备刷新后请重试");
                setDevices();
                return false;
            }
        }
        return true;
    }


    /**
     * 指定设备
     */
    public static void setDevices() {
//        Thread t = new Thread(new Runnable() {
//            public void run() {
//                setDevices(devicesInfo());
//            }
//        }, "setDevices");
//        t.start();
        setDevices(devicesInfo());
    }

    /**
     * 指定设备
     *
     * @param deivcesInfo
     */
    public static void setDevices(String[] deivcesInfo) {
        if (deivcesInfo.length == 0) {
            if (HomePage.textArea != null) {
                HomePage.textArea.setText("没有连接设备");
            }
        } else if (deivcesInfo.length == 1) {
            if (deivcesInfo[0].contains("unauthorized") || deivcesInfo[0].contains("connecting")) {
                if (HomePage.textArea != null) {
                    HomePage.textArea.setText("设备没有开放权限");
                }
            } else {
                devices =
                        (deivcesInfo[0].substring(0, deivcesInfo[0].indexOf(" "))).trim();
                if (HomePage.textArea != null) HomePage.textArea.setText(
                        deivcesInfo[0].substring(deivcesInfo[0].indexOf("model") + 6,
                                deivcesInfo[0].lastIndexOf("device")
                        ));
            }
        } else {
            if (HomePage.textArea.getText().contains("用户手动关闭")) return;
            String[] devicesArr = devicesInfo();
            if (devicesArr == null) return;
            if (Arrays.toString(devicesArr).contains(HomePage.textArea.getText()) &&
                    !HomePage.textArea.getText().equals("")) return;
            String de = TooltipUtil.listSelectTooltip("发现" + deivcesInfo.length + "设备,请选择一个设备", devicesArr);
            if (de == null) {
                HomePage.textArea.setText("用户手动关闭");
                return;
            }
            if (de.contains("unauthorized") || de.contains("connecting")) {
                if (HomePage.textArea != null) {
                    HomePage.textArea.setText("设备没有开放权限");
                }
            } else {
                devices =
                        (de.substring(0, de.indexOf(" "))).trim();
                if (HomePage.textArea != null) HomePage.textArea.setText(
                        de.substring(de.indexOf("model") + 6,
                                de.lastIndexOf("device"))
                );
            }
        }
    }
}
//