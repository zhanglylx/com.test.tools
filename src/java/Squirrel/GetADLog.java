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
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class GetADLog extends Pane {
    public static String[] AD;
    Thread threadAdLog;
    JTextArea logPaint = null;
    private JButton clear;
    //判断线程是否在运行
    private boolean runT = false;
    //判断是否打印了没有获取日志的错误信息,禁止重复打印
    boolean getLog = false;
    private Log log;

    /**
     * 设置广告位
     */
    static {
        AD = new String[0];
        for (int i = 0; i < 72; i++) {
            AD = Arrays.copyOf(AD, AD.length + 1);
            AD[AD.length - 1] = "GG-" + i;
        }
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
        this.clear = new JButton("清空");
        jPa.add(this.clear);
        setButton(clear);
        add(jPa);
        setJDialog();
        this.log = new Log();
        threadAdLog = new Thread(log);
        threadAdLog.setDaemon(true);
    }

    /**
     * 设置窗体
     */
    public void setJDialog() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                threadAdLog.interrupt();
                log.closeLog();
                TestTools.setJButtonEnabled(getTitle());
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
            if (f == this.clear) {
                logPaint.setText("");
                return;
            }
            f.setBackground(Color.magenta);
            log.restoreColor();
            logPaint.setText("请等待，正在刷新:" + f.getText() + "\n");
            if (threadAdLog.isInterrupted()) threadAdLog.interrupt();
            log.setAdIdRecord(f.getText());
            if (!threadAdLog.isAlive()) threadAdLog.start();
            log.setjButton(f);
        });

    }

    class Log implements Runnable {

        private String[] GG;
        private int n;
        private String date;
        private String adIdRecord;//记录广告是否被切换
        private boolean stopLog;
        private JButton jButton;
        //用于判断广告的长度，两次长度不一样打印到页面
        private int GGlen = 0;

        public Log() {
            this.date = date();
            this.n = 0;
            this.adIdRecord = "";
            this.stopLog = true;
            this.jButton = new JButton();
        }

        public void setAdIdRecord(String adIdRecord) {
            this.adIdRecord = adIdRecord;
            this.GGlen = 0;
        }

        public void setjButton(JButton jButton) {
            this.jButton = jButton;
        }

        public void restoreColor() {
            this.jButton.setBackground(Color.orange);
        }

        public void closeLog() {
            this.stopLog = false;
        }

        public String getAdIdRecord() {
            return this.adIdRecord;
        }

        @Override
        public void run() {
            boolean bl;
            while (this.stopLog) {
                bl = false;
                GG = AdbUtils.operationAdb(" shell cat /sdcard/FreeBook/ad/" + date + "/" + adIdRecord + ".txt");
                System.out.println(" shell cat /sdcard/FreeBook/ad/" + date + "/" + adIdRecord + ".txt");
                if (this.GG == null) addText("获取失败，重试");
                if (GG.length > GGlen) {
                    for (int i = GGlen; i < GG.length; i++) {
                        if (GG[i].equals("")) continue;
                        if (logPaint.getText().endsWith(".")) logPaint.append("\n");
                        addText(date + "  " + GG[i]);
                        if ((GG[i].contains("返回广告") && !GG[i].contains("服务端返回广告"))
                                || GG[i].contains("服务端返回广告---没有广告") ||
                                GG[i].contains("请求失败") || GG[i].contains("No such file or directory")) bl = true;
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
                }
                if(bl)logPaint.append(".");
            }
        }
    }

    /**
     * 向文本编辑区添加文字
     *
     * @param text
     */
    public void addText(String text) {
        if (text.contains("=")) text += "     " + log.getAdIdRecord();
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
        } catch (Exception e) {
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
