package ZLYUtils;
import	java.util.Map;

import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;

public class ZLYThreadUtils {
    /**
     * 获取当前线程id
     */
    public static Long getThreadId() {
        try {
            return Thread.currentThread().getId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前线程名称
     */
    public static String getThreadName() {
        try {
            return Thread.currentThread().getName();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取当前进程id
     */
    public static Long getProcessId() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            String name = runtime.getName();
            String pid = name.substring(0, name.indexOf('@'));
            return Long.parseLong(pid);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前服务器ip地址
     */
    public static String getServerIp() {
        try {
            //用 getLocalHost() 方法创建的InetAddress的对象
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前调用者方法名称
     *
     * @return
     */
    public static String getMethodName() {
        //第一种方法
//        StackTraceElement[] stackTrace1 = new Exception().getStackTrace();
//        String methodName = null;
//        if (stackTrace1 != null && stackTrace1.length > 0) {
//            methodName = stackTrace1[stackTrace1.length-1].getMethodName();
//        }
        StringBuilder stringBuilder = new StringBuilder();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int number = stackTrace.length > 10 ? 9 : stackTrace.length-1;
        for (int i = number; i >= 0; i--) {
            stringBuilder.append(stackTrace[i].getMethodName());
            stringBuilder.append("->");
        }
        return stringBuilder.length() == 0 ? null : StringUtils.chop(StringUtils.chop(stringBuilder.toString()));
    }

}
