package AdConfiguration;

import ZLYUtils.JavaUtils;
import ZLYUtils.Network;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.rmi.ServerException;
import java.util.*;

/**
 * 获取广告类型
 */
public class GetAppType implements Runnable {
    private AdUi adUi;
    private Loging loging;
    private Thread threadLoging;

    public Vector getVector() {
        return vector;
    }

    private Vector vector;

    public Map<String, String> getAdNoMap() {
        return adNoMap;
    }

    private Map<String, String> adNoMap;

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
            this.vector.add("获取失败:" + appName);
            this.adUi.getAppType().setModel(
                    new DefaultComboBoxModel(this.vector));
            this.adUi.getBuiltInAppType().setModel(
                    new DefaultComboBoxModel(this.vector));
        } else {
            this.closeLoging();
            for (Map.Entry<String, String> entry : this.adNoMap.entrySet()) {
                this.vector.add(entry.getKey());
            }
            //内置广告
            Vector builtInAppType = new Vector();
            builtInAppType.add("请选择一条内置广告");
            for (String s : AdSendConfig.getBuiltInAppType(this.appName)) {
                if (this.adNoMap.containsKey(s)) {
                    builtInAppType.add(s);
                } else if (s.equals(AdSendConfig.ZHI_TOU)) {
                    builtInAppType.add(s);
                }
            }
            if (builtInAppType.size() == 1) {
                builtInAppType.add("未找到内置类型:" +
                        Arrays.toString(AdSendConfig.getBuiltInAppType(this.appName)));
                this.adUi.getBuiltInAppType().setEnabled(false);
            }
            this.adUi.getBuiltInAppType().setModel(
                    new DefaultComboBoxModel(builtInAppType));
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
        //发送获取广告类型请求
        String response = Network.sendPost(
                AdSendConfig.HOST_TEST + AdSendConfig.GET_APP_TYPE_PATH
                        + "?ran=" + Math.random()
                , "appname=" + AdSendConfig.getAppNameCode(this.appName)
                , AdSendConfig.HEADERS);
        try {
            Document doc = Jsoup.parse(response);
            Elements elements = doc.select("option");
            for (int i = 0; i < elements.size(); i++) {
                this.adNoMap.put(elements.get(i).text(),
                        elements.get(i).attributes().get("value"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (this.adNoMap.size() > 0) {
            return true;
        }
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
            adUi.getBuiltInAppType().setEnabled(false);
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
            adUi.getBuiltInAppType().setEnabled(true);
            this.stop = true;
        }
    }
}
