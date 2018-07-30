package Squirrel;

import SquirrelFrame.*;
import SquirrelFrame.Pane;
import ZLYUtils.AdbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class GetADLog extends Pane {
    public static String[] AD;
    //广告id
    public static String adId = null;
    public WindowsText windowsText;
    public static ArrayList<String> list;
    Thread t;
    JTextArea logPaint = null;
    //判断线程是否在运行
    private boolean runT = false;
    //用于判断广告的长度，两次长度不一样打印到页面
    private int GGlen = 0;
    //判断是否打印了没有获取日志的错误信息,禁止重复打印
    boolean getLog = false;

    /**
     * 设置广告位
     */
    static {
        AD = new String[0];
        for (int i = 0; i < 72; i++) {
            AD = Arrays.copyOf(AD, AD.length + 1);
            AD[AD.length - 1] = "GG-" + i;
        }
        list = new ArrayList<>();
    }

    public GetADLog(String buttonText, JDialog frame) {
        super(buttonText, frame);
        setLayout(new GridLayout(2, 10));
        JPanel jPa = new JPanel();
        jPa.setLayout(new GridLayout(10, 10));
        for (int i = 0; i < AD.length; i++) {
            JButton jb = new JButton(AD[i]);
            jPa.add(jb);
            setButton(jb);
        }
        add(jPa);
        setJDialog();
    }

    /**
     * 设置窗体
     */
    public void setJDialog() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windowsClose = true;
                super.windowClosing(e);
                setDefaultCloseOperation(2);
            }
        });
        setSize(1000, 700);
        setLocation(400, 200);
        setLocationRelativeTo(null);
        logPaint = new JTextArea();
        logPaint.setLineWrap(true);
        logPaint.setWrapStyleWord(true);
        logPaint.setEditable(false);
        JScrollPane jsc = new JScrollPane(logPaint);
        logPaint.setFont(new Font("标楷体", Font.BOLD, 20));
        add(jsc);
        setVisible(true);

    }

    /**
     * 设置按钮监听器
     *
     * @param f
     */
    public void buttonMouseListener(JButton f) {
        f.addActionListener((ActionEvent e) -> {
            f.setBackground(Color.magenta);
            adId = f.getText();
            GGlen = 0;
            logPaint.setText("请等待，正在刷新:" + f.getText() + "\n");
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!runT) {
                        log();

                    }
                }
            });
            t.start();
            getLog = false;
        });

    }

    public void log() {
        runT = true;
        String[] GG;
        int n = 0;
        String date = date();
        String adIdRecord = "";//记录广告是否被切换
        while (true) {
            if (logPaint == null) continue;
            if (windowsClose) {
                windowsClose = false;
                break;
            }
            //切换广告后，置为默认值
            if (!adIdRecord.equals(adId)) {
                GGlen = 0;
                adIdRecord = adId;
            }
            boolean bl = false;
            GG = AdbUtils.operationAdb(" shell cat /sdcard/FreeBook/ad/" + date + "/" + adId + ".txt");
            System.out.println(" shell cat /sdcard/FreeBook/ad/" + date + "/" + adId + ".txt");
            if (Arrays.toString(GG).contains("errdevices")) break;
            if (Arrays.toString(GG).equals("[]") && !getLog) {
                getLog = true;
                addText("没有获取到日志:" + adId + Arrays.toString(GG));
            }
            if (GG == null || GG.length < 1) continue;
            if (GG.length > GGlen) {
                for (int i = GGlen; i < GG.length; i++) {
                    if (GG[i].equals("")) continue;
                    addText(date + "  " + GG[i]);
                    if ((GG[i].contains("返回广告") && !GG[i].contains("服务端返回广告"))
                            || GG[i].contains("服务端返回广告---没有广告") ||
                            GG[i].contains("请求失败")) bl = true;
                }
                GGlen = GG.length;
            }

            if (bl) {
                switch (n) {
                    case (0):
                        addText("=========");
                        n = 1;
                        break;
                    case (1):
                        addText("==================");
                        n = 2;
                        break;
                    case (2):
                        addText("====================================");
                        n = 3;
                        break;
                    case (3):
                        addText("======================================================");
                        n = 0;
                        break;
                }

            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        runT = false;
    }

    /**
     * 向文本编辑区添加文字
     *
     * @param text
     */
    public void addText(String text) {
        if (text.contains("=")) text += "     " + adId;
        logPaint.append(text + "\n");
//        //下面的代码就是移动到文本域的最后面
        logPaint.selectAll();
        if (logPaint.getSelectedText() != null && logPaint != null) {
            logPaint.setCaretPosition(logPaint.getSelectedText().length());
            logPaint.requestFocus();
        }
    }

    private static String date() {
        //因通过手机过滤时间时发现日志转换不正确，所以采用获取点击方式
        try {
            for (String s : AdbUtils.operationAdb(" shell date")) {
                if (s.matches(".+\\d{2}:\\d{2}:\\d{2}.+")) {
                    s = s.replace("CST", "");
                    SimpleDateFormat sfEnd = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sfStart = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
                    try {
                        return sfEnd.format(sfStart.parse(s));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 设置按钮
     *
     * @param text
     */
    public void setButton(JButton text) {
        buttonMouseListener(text);
    }
}
