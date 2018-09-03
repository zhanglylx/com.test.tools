package ZLYUtils;

import java.io.File;
import java.io.IOException;

public class JavaUtils {
    /**
     * 睡眠
     *
     * @param time
     */
    public static void sleep(Long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
        }
    }

    /**
     * 获取文件后缀名称
     * @return
     */
    public static String getFileSuffixName(File file) throws IllegalArgumentException {
        if (file == null) throw new IllegalArgumentException("文件为空");
        if (!file.exists()) throw new IllegalArgumentException("文件不存在:" + file.getPath());
        if (file.getName().lastIndexOf(".") == -1) throw
                new IllegalArgumentException("不是一个文件:"+file.getName());
        return file.getName().substring(
                file.getName().lastIndexOf(".")+1,file.getName().length());
    }


    public static void sleep(int time) {
        sleep((long) time);
    }

    /**
     * java方法
     */
    private void fangfa() {

        try {
            System.in.read();//按任意键
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
