package ZLYUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IOUtils {
    /**
     * 读取给定的文件，并将数据已字节数组的形式返回
     */
    public static byte[] loadBytes(File src)
            throws IOException {
        if (src == null) throw new IllegalArgumentException("文件为空！");
        if (!src.exists()) throw new IllegalArgumentException(src + "不存在！");
        FileInputStream fis = null;//创建文件输入流
        try {
            fis = new FileInputStream(src);
//			byte[] data = new byte[(int)src.length()];
            /**
             * FileInputStream的available()方法；
             * 返回当前字节输入流可读取的总字节数
             */
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        } catch (IOException e) {
            throw e;//读取出错误将异常抛给调用者解决
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

    }
}
