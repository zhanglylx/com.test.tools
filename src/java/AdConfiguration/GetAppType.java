package AdConfiguration;

import javax.swing.*;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 获取广告类型
 */
public class GetAppType implements Runnable {
    private AdUi adUi;
    private Loging loging;
    private Thread threadLoging;
    private Vector vector;
    private Map<String,String> adNoMap;
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    private String appName;

    public GetAppType(AdUi adUi) {
        this.adUi = adUi;
        this.appName = this.adUi.getAppName().getSelectedItem().toString();
        this.loging = new Loging();
        this.vector = new Vector();
        this.adNoMap = new LinkedHashMap<>();
    }

    @Override
    public void run() {
        this.threadLoging = new Thread(loging);
        this.threadLoging.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.vector.clear();
        if (!sendAppTypeAdNo()) {
            this.closeLoging();
            this.vector.add("获取失败:"+appName);
            this.adUi.getAppType().setModel(
                    new DefaultComboBoxModel(this.vector));
        }else{
            this.closeLoging();
            for(Map.Entry<String,String> entry : this.adNoMap.entrySet()){
                this.vector.add(entry.getKey());
            }
            this.adUi.getAppType().setModel(
                    new DefaultComboBoxModel(this.vector));
            adUi.getSb().setEnabled(true);
        }

    }

    /**
     * 向服务端获取app类型的编号
     *
     * @return
     */
    private boolean sendAppTypeAdNo() {
        this.adNoMap.clear();
        this.adNoMap.put("广点通","15154564564654");
        this.adNoMap.put("头条","33334245");
        if(this.adNoMap.size()>0)return true;
        return false;
    }

    public void closeLoging() {
        this.loging.close();
        this.threadLoging.interrupt();
        while (true) {
            if (this.threadLoging.isAlive()) break;
        }

    }

    class Loging implements Runnable {
        private boolean stop;

        public Loging() {
            this.stop = true;
        }

        public void close() {
            this.stop = false;
        }

        @Override
        public void run() {
            int i = 0;
            Vector vector = new Vector();
            String str = "正在请求数据中:";
            vector.add(str + appName);
            StringBuffer stringBuffer = new StringBuffer();
            adUi.getAppType().setEnabled(false);
            adUi.getAppName().setEnabled(false);
            adUi.getSb().setEnabled(false);
            while (this.stop) {
                //大于指定次数，重新计算.
                if (i > 30) {
                    stringBuffer.append(str);
                    stringBuffer.append(appName);
                    stringBuffer.append(".");
                    vector.add(stringBuffer);
                    i = 0;
                } else {
                    stringBuffer.append(vector.get(0));
                    stringBuffer.append(".");
                    vector.add(stringBuffer);
                }
                //删除上一次的数据，始终保持存在一条最新loging数据
                vector.remove(0);
                adUi.getAppType().setModel(new DefaultComboBoxModel(vector));
                //将流清空
                stringBuffer = new StringBuffer();
                i++;
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                }
            }
            adUi.getAppType().setEnabled(true);
            adUi.getAppName().setEnabled(true);
            adUi.getSb().setEnabled(true);
            this.stop = true;
        }
    }
}
