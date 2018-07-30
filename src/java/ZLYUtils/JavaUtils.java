package ZLYUtils;

import java.io.IOException;

public class JavaUtils {
    /**
     * 睡眠
     * @param time
     */
    public static void sleep(Long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
        }
    }
    public static void sleep(int time){
        sleep((long)time);
    }
    /**
     * java方法
     */
    private void fangfa(){

        try {
            System.in.read();//按任意键
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
