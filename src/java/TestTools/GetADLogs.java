package TestTools;

import Frame.FrontPanel;
import ZLYUtils.AdbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GetADLogs extends FrontPanel {
    public static String[] AD;
    Thread threadAdLog;
    JTextArea logPaint = null;
    private JButton clear;
    //判断线程是否在运行
    private boolean runT = false;
    private Log log;
    private List<JButton> jButtonsAd;
    private JButton jButton;
    /**
     * 设置广告位
     */
    static {
        AD = new String[0];
        for (int i = 1; i < 90; i++) {
            AD = Arrays.copyOf(AD, AD.length + 1);
            AD[AD.length - 1] = "GG-" + i;
        }
    }

    public GetADLogs(String buttonText,JButton jButton) {
        super(buttonText);
        this.jButton=jButton;
        this.jButton.setEnabled(false);
        setLayout(new GridLayout(2, 10));
        JPanel jPa = newJPanel();
        jPa.setLayout(new GridLayout(10, 20));
        JButton jb;
        this.jButtonsAd = new ArrayList<>();
        for (int i = 0; i < AD.length; i++) {
            jb = newJButton(AD[i],true,true);
            jPa.add(jb);
            this.jButtonsAd.add(jb);
        }
        this.clear = newJButton("清空");
        jPa.add(this.clear);
        add(jPa);
        setJDialog();
        this.log = new Log();
        threadAdLog = new Thread(log);
    }

    @Override
    public int setClose() {
        threadAdLog.interrupt();
        log.closeLog();
        this.jButton.setEnabled(true);
        return 2;
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
     * 限制每个按钮间快速切换
     */
    public void limitButtonClick() {
        for (JButton jButton : this.jButtonsAd) {
            jButton.setEnabled(false);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (JButton jButton : this.jButtonsAd) {
            jButton.setEnabled(true);
        }

    }


    @Override
    public void jRadioButtonClickEvent(JRadioButton jRadioButton) {

    }

    @Override
    public void jTextFieldEnteredEvent(JTextField jTextField) {

    }

    @Override
    public void jTextFieldReleasedEvent(JTextField jTextField) {

    }

    @Override
    public void jTextFieldExitedEvent(JTextField jTextField) {

    }

    @Override
    public void jTextFieldInputEvent(JTextField jTextField, KeyEvent e) {

    }

    @Override
    public void jTextFieldPressedEvent(JTextField jTextField) {

    }

    @Override
    public void jTextFieldClickEvent(JTextField jTextField) {

    }

    @Override
    public void buttonClickEvent(JButton f) {
        if (f == this.clear) {
            setLogPaint("");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                limitButtonClick();
            }
        }).start();
        log.setAdIdRecord(f.getText());
        f.setBackground(Color.magenta);
        if (f != log.jButton) log.restoreColor();
        log.clearStringBuffer();
        if (!threadAdLog.isAlive()) {
            threadAdLog = new Thread(log);
            threadAdLog.start();
        } else {
            threadAdLog.interrupt();
        }
        this.setLogPaint("正在刷新，请稍后:" + f.getText());
        log.setjButton(f);
    }


    @Override
    public void jComboBoxClickEvent(JComboBox jComboBox) {

    }

    @Override
    public void jComboBoxPopupMenuCanceled(JComboBox jComboBox) {

    }

    @Override
    public void jComboBoxPopupMenuWillBecomeInvisible(JComboBox jComboBox) {

    }

    @Override
    public void jComboBoxDeselectedItem(String str) {

    }

    @Override
    public void jComboBoxSelectedItem(String str) {

    }

    @Override
    public void jComboBoxPopupMenuWillBecomeVisible(JComboBox jComboBox) {

    }


    class Log implements Runnable {
        private int n;
        private String date;
        private String adIdRecord;//记录广告是否被切换
        private boolean stopLog;
        private JButton jButton;
        private String getErrText = "获取失败，重试中,可能是没有连接手机";
        private StringBuffer stringBuffer;//记录显示台已经显示的信息

        public Log() {
            this.date = date();
            this.n = 0;
            this.adIdRecord = "";
            this.stopLog = true;
            this.jButton = newJButton("");
            this.stringBuffer = new StringBuffer();
        }

        public void clearStringBuffer() {
            this.stringBuffer = new StringBuffer();
        }

        public void setAdIdRecord(String adIdRecord) {
            this.adIdRecord = adIdRecord;
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
            boolean err = false;
            boolean errSucceed = false;
            boolean b = false; //用于在每次正在刷新打印-时，有新结果换行显示
            String ggStr = this.adIdRecord;
            String[] GG;
            while (this.stopLog) {
                logPaintAppend("==");
                GG = AdbUtils.runAdb(" shell cat /sdcard/FreeBook/ad/" + date + "/" + adIdRecord + ".txt");
                System.out.println(" shell cat /sdcard/FreeBook/ad/" + date + "/" + adIdRecord + ".txt");
                if (GG == null) {
                    if (!err) addText(this.getErrText);
                    logPaintAppend(".");
                    err = true;
                    errSucceed = true;
                    continue;
                }
                err = false;
                if (errSucceed) addText("重试成功");
                errSucceed = false;
                b = true;
                for (int i = 0; i < GG.length; i++) {
                    //判断显示台是否已经显示当前信息
                    if (!stringBuffer.toString().contains(date + "  " + GG[i])) {
                        //判断是否切换广告，如果切换，当前取消打印
                        if (ggStr.equals(this.adIdRecord)) {
                            if (b) {
                                addText("");
                                b = false;
                            }
                            //判断是否已经显示过错误信息，如果没有，则将显示信息添加
                            if (GG[i].contains("No such file or directory")) {
                                if (!stringBuffer.toString().contains(date)) {
                                    stringBuffer.append(date + "  " + GG[i]);
                                }
                            }
                            stringBuffer.append(date + "  " + GG[i]);
                            addText(date + "  " + GG[i]);
                        } else {
                            break;
                        }
                    }
//                    if(i==GG.length-1 && !GG[i].contains("No such file or directory")){
//                        System.out.println("---");
//                        AdbUtils.runAdb(" shell rm -r /sdcard/FreeBook/ad/" + date + "/" + adIdRecord + ".txt");
//                    }
                }
                ggStr = this.adIdRecord;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 向文本编辑区添加文字
     *
     * @param text
     */
    public synchronized void addText(String text) {
        if (text.contains("=")) text += "     " + log.getAdIdRecord();
        logPaint.append(text + "\n");
//        //下面的代码就是移动到文本域的最后面
        logPaint.selectAll();
        if (logPaint.getSelectedText() != null && logPaint != null) {
            logPaint.setCaretPosition(logPaint.getSelectedText().length());
            logPaint.requestFocus();
        }
    }

    public synchronized void logPaintAppend(String text) {
        logPaint.append(text);
    }

    public synchronized String getLogPaintText() {
        return logPaint.getText();
    }

    public synchronized void setLogPaint(String text) {
        logPaint.setText(text);
    }

    private static String date() {
        //因通过手机过滤时间时发现日志转换不正确，所以采用获取电脑时间方式
//        try {
//            for (String s : AdbUtils.operationAdb(" shell date")) {
//                if (s.matches(".+\\d{2}:\\d{2}:\\d{2}.+")) {
//                    s = s.replace("CST", "");
//                    SimpleDateFormat sfEnd = new SimpleDateFormat("yyyy-MM-dd");
//                    SimpleDateFormat sfStart = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
//                    try {
//                        return sfEnd.format(sfStart.parse(s));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }


}
