package Squirrel;

import InterfaceTesting.SquirrelUi;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class TestTools {
    public  static  Map<String,JButton> jButtonMap = new HashMap<>();
    private static Map<String,Boolean> jButtonMapEnabled = new HashMap<>();
    public static void invokingTestFrame(String testFrame, JDialog jdialog,JButton jButton) {
        //InterfaceTesting可以多开
        if(!InterfaceTesting.equals(jButton.getText()))setjButtonEnabledFalse(jButton);
        switch (testFrame) {
            case getADLog:
                new GetADLog(testFrame, jdialog);
                break;
            case leaveBug:
                new LeaveBug(leaveBug, jdialog);
                break;
            case ZhiBoTools:
                new ZhiBo(ZhiBoTools, jdialog);
                break;
            case Video_RECORDING_AND_SCREENSHOT:
                new VideoRecordingScreenshot(Video_RECORDING_AND_SCREENSHOT,jdialog);
                break;
            case InterfaceTesting:
                new SquirrelUi(InterfaceTesting);
                break;
        }
    }

    public synchronized static void setjButtonEnabledFalse(JButton jButton){
        jButtonMap.put(jButton.getText(),jButton);
        jButtonMapEnabled.put(jButton.getText(),false);
        jButton.setEnabled(false);
    }

    public synchronized static void setJButtonEnabled(String title){
        jButtonMap.get(title).setEnabled(true);
        jButtonMapEnabled.put(title,true);
    }

    public  static Map<String,Boolean> getJButtonMapEnabled(){
        return jButtonMapEnabled;
    }

    public static final String getADLog = "获取广告日志";
    public static final String leaveBug = "版本遗留bug";
    public static final String ZhiBoTools = "直播工具";
    public static final String Video_RECORDING_AND_SCREENSHOT = "录屏与截图";
    public static final String InterfaceTesting = "接口测试工具";
    public static String[] testTools = new String[]{getADLog, leaveBug, ZhiBoTools,
            Video_RECORDING_AND_SCREENSHOT,InterfaceTesting};
    ;
}
