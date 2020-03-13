package performanceTest;

import ZLYUtils.TooltipUtil;
import ZLYUtils.Uiautomator;

import java.io.IOException;

/**
 * 获取性能数据
 */
public class RunMainPerformance {
    public static void main(String[] args) throws IOException {
        //QQ阅读包名:com.qq.reader
        //掌阅:com.chaozh.iReaderFree
        //中文书城:com.chineseall.singlebook
        Statistical statistical = new Statistical("com.mfyueduqi.book");
        GetIphoneData getIphoneData = new GetIphoneData(statistical);
        boolean[] b = new boolean[1];
        b[0] = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int index = 1;
                while (true) {
                    if(TooltipUtil.yesNoTooltip("是否关闭获取性能")==0){
                        b[0]=false;
                        break;
                    }
//                    Uiautomator.inputTap(1011, 50);
//                    if (index == 100) {
//                        b[0] = false;
//                        break;
//                    }
//                    index++;
                    Uiautomator.inputHome();
                    try {
                        Thread.sleep(10*60*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    b[0]=false;
                    break;
                }
            }
        }).start();
        while (b[0]) {
            getIphoneData.run();
            System.out.println(statistical.toString());
        }
    }
}
