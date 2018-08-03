package Squirrel;

import SquirrelFrame.FrameSqiorrel;
import SquirrelFrame.OutputText;
import ZLYUtils.FrameUtils;
import ZLYUtils.SaveCrash;
import ZLYUtils.TooltipUtil;
import ZLYUtils.WindosUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import static Squirrel.Backpack_gift.refreshEndTherad;
import static Squirrel.ZhiBo.backpack_gift;

/**
 * 直播工具类
 */
public class ZhiBo extends FrameSqiorrel {
    public static final String backpack_gift = "获取背包礼物";
    public static final String[] zhiboArrays;

    static {
        zhiboArrays = new String[]{backpack_gift};
    }

    public ZhiBo(String title, JDialog jdialog) {

        super(title, jdialog);
        addJButton(zhiboArrays);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { //设置退出监听器
                super.windowClosing(e);
                setDefaultCloseOperation(2);
                TestTools.setJButtonEnabled(getTitle());
            }
        });
        setVisible(true);
    }

    @Override
    public void addButtonMouseListener(JButton f) {
        f.addActionListener(e -> {
            String text = f.getText();
            switch (text) {
                case backpack_gift:
                    new Backpack_gift(this);
            }
        });
    }
}

/**
 * 背包礼物
 */
class Backpack_gift extends JDialog {
    //输出框
    private OutputText opt;
    private JTextField useridText;
    private JButton submit;
    private JButton all;
    private String userID;
    private ZhiBoDataBase zbdb;
    public static volatile boolean refreshEndTherad;//判断线程是否结束,true为关闭刷新线程
    private JButton clear;//清空按钮
    private JButton automationRefresh;//自定刷新
    private boolean automationRefreshThread;//判断自动刷新是否在执行,false为关闭自动刷新线程
    private boolean automationRefreshThreadClose;//确认刷新线程关闭,false为正在执行，true为关闭
    public JButton refresh;//刷新按钮
    private Refresh refreshClass;
    static {
        refreshEndTherad = false;
    }

    public Backpack_gift(JDialog jDialog) {
        super(jDialog, true);
        refresh = new JButton();
        refreshClass = new Refresh(refresh);
        automationRefreshThread = false;
        automationRefreshThreadClose = true;
        setTitle(backpack_gift);
        setResizable(false);//禁止拖拽大小
        setLayout(null);//手动设置布局
        setSize(900, 500);
        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel1.add(new JLabel("请输入userid:"));
        userID = null;
        useridText = new JTextField(8);
        jPanel1.add(useridText);
        jPanel1.add((submit = new JButton("提交")));
        jPanel1.add((all = new JButton("全部")));
        addJButtonMonitor(all);//给全部按钮添加监听器
        jPanel1.add((automationRefresh = new JButton("自动刷新")));
        addJButtonMonitor(automationRefresh);//给自动刷新按钮添加监听器
        refresh = FrameUtils.jbuttonImage("image/refresh/0.png");
        jPanel1.add((clear = new JButton("清屏")));
        addJButtonMonitor(clear);
        addJButtonMonitor(refresh);//给刷新按钮添加监听器
        jPanel1.add(refresh);
        addJButtonMonitor(submit);//给提交按钮添加监听器
        jPanel1.setSize(new Dimension(this.getWidth(), 35));
        jPanel1.setBorder(BorderFactory.createEtchedBorder());//设置边框
        add(jPanel1);
        opt = new OutputText();
        JScrollPane jsc = opt.getJsc();
        jsc.setSize(this.getWidth(), this.getHeight() - 100);
        jsc.setLocation(this.getX(), this.getY() + 50);
        setLocationRelativeTo(null);
        add(jsc);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                refreshEndTherad = true;//关闭刷新图标
                automationRefreshThread = false;//关闭自动刷新线程
                jDialog.setDefaultCloseOperation(2);


            }
        });
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                zbdb = ZhiBoDataBase.getZhiBoDataBase(opt);
            }
        });
        t.start();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //判断数据库是否连接成功，不成功按钮为false
                    if (zbdb == null || !zbdb.getdataBaseOnline()) {
                        submit.setEnabled(false);
                        all.setEnabled(false);
                        automationRefresh.setEnabled(false);
                    } else {
                        submit.setEnabled(true);
                        all.setEnabled(true);
                        automationRefresh.setEnabled(true);
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();
        setVisible(true);

    }

    /**
     * 给按钮添加监听器
     */
    public void addJButtonMonitor(JButton j) {
        j.addActionListener(e -> {
            //判断是否为提交按钮
            if (j == submit) {
                checkUseridText();
            } else if (j == all) {
                userID = "*";
                runSelect();
            } else if (j == clear) {
                opt.setText("");
            } else if (j == automationRefresh) {
                //判断自动刷新是否在执行
                if ("自动刷新".equals(j.getText())) {
                    automationRefreshThread = true;
                    Thread th = new Thread(refreshClass);
                    th.start();
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            automationRefreshSelectData();
                        }
                    });
                    t.start();
                    submit.setEnabled(false);
                    all.setEnabled(false);
                    automationRefresh.setText("停止");
                } else {
                    automationRefreshThread = false;
                    refreshEndTherad = true;
                    submit.setEnabled(true);
                    all.setEnabled(true);
                    automationRefresh.setText("自动刷新");

                }
            }


        });
    }

    /**
     * 检查输入是否合法
     */
    public void checkUseridText() {
        //检测输入userid是否合法
        if (!useridText.getText().matches("\\d+") && !"*".equals(useridText.getText())) {
            TooltipUtil.errTooltip("输入的userId不正确，请输入正确的userID或*");
            return;
        } else {
            this.userID = useridText.getText();
        }
        runSelect();
    }

    /**
     * 执行查询
     */
    public void runSelect() {
        submit.setEnabled(false);
        all.setEnabled(false);
        automationRefresh.setEnabled(false);
        Thread t = new Thread();
        t.start();
        Thread se = new Thread(new Runnable() {
            @Override
            public void run() {
                selectData();
                opt.addText("====================执行完毕:" + WindosUtils.getDate() + "====================\n");
                submit.setEnabled(true);
                all.setEnabled(true);
                automationRefresh.setEnabled(true);
            }
        });
        se.start();
    }


    /**
     * 查询礼物数据
     */
    public void selectData() {
        ResultSet rs;
        try {
            if (userID == null || ("*").equals(userID)) {
                rs = zbdb.getCdb().selectSql("select * from biz_backpack_gift");
                opt.addText("select * from biz_backpack_gift\n");
            } else {
                rs = zbdb.getCdb().selectSql("select * from biz_backpack_gift where user_id = " + userID);
                opt.addText("select * from biz_backpack_gift where user_id = " + userID + "\n");
            }
            ResultSetMetaData data = rs.getMetaData();
            ArrayList<String> list = new ArrayList<>();
            while (rs.next()) {
                StringBuffer stb = new StringBuffer();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    //获得所有列的数目及实际列数
                    int columnCount = data.getColumnCount();
                    //获得指定列的列名
                    String columnName = data.getColumnName(i);
                    if (!"bp_gift_name".equals(columnName) &&
                            !"bp_gift_price".equals(columnName) &&
                            !"bp_gift_num".equals(columnName) &&
                            !"user_id".equals(columnName) &&
                            !"bp_gift_id".equals(columnName)) continue;
                    switch (columnName) {
                        case "bp_gift_name":
                            columnName = "name";
                            break;
                        case "bp_gift_price":
                            columnName = "price";
                            break;
                        case "bp_gift_num":
                            columnName = "num";
                            break;
                        case "bp_gift_id":
                            columnName = "id";
                            break;
                    }
                    //获得指定列的列值
                    String columnValue = rs.getString(i);
                    String str = columnName + ":" + columnValue;
                    if (!"id".equals(columnName)) str = StringUtils.leftPad(str, 15, " ");
                    stb.append(str);
                    //获得指定列的数据类型
                    int columnType = data.getColumnType(i);
                    //获得指定列的数据类型名
                    String columnTypeName = data.getColumnTypeName(i);
                    //所在的Catalog名字
                    String catalogName = data.getCatalogName(i);
                    //对应数据类型的类
                    String columnClassName = data.getColumnClassName(i);
                    //在数据库中类型的最大字符个数
                    int columnDisplaySize = data.getColumnDisplaySize(i);
                    //默认的列的标题
                    String columnLabel = data.getColumnLabel(i);
                    //获得列的模式
                    String schemaName = data.getSchemaName(i);
                    //某列类型的精确度(类型的长度)
                    int precision = data.getPrecision(i);
                    //小数点后的位数
                    int scale = data.getScale(i);
                    //获取某列对应的表名
                    String tableName = data.getTableName(i);
                    // 是否自动递增
                    boolean isAutoInctement = data.isAutoIncrement(i);
                    //在数据库中是否为货币型
                    boolean isCurrency = data.isCurrency(i);
                    //是否为空
                    int isNullable = data.isNullable(i);
                    //是否为只读
                    boolean isReadOnly = data.isReadOnly(i);
                    //能否出现在where中
                    boolean isSearchable = data.isSearchable(i);
                    stb = addStrBuffer(stb, "");

                }
                list.add(stb.toString());
            }
            Iterator<String> iterable = list.iterator();
            int index = 1;
            while (iterable.hasNext()) {
                opt.addText(index + "：" + iterable.next() + "\n");
                index++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            opt.setText("查询发生异常");
        } finally {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            refreshEndTherad = true;
        }
    }


    /**
     * 自动刷新
     */
    public synchronized void automationRefreshSelectData() {
        automationRefreshThreadClose = false;//打开线程状态
        ResultSet rs;
        ArrayList<String> recordList = new ArrayList<>();//记录已经打印的数据
        //检测输入用户是否输入了id，没有输入或者不合法给默认值
        if (useridText.getText().matches("\\d+")) {
            this.userID = useridText.getText();
        } else {
            this.userID = "*";
        }
        int sqlPrint = 0;//判断SQL语句是否已经打印
        int logPrint = 2;//记录正在刷新log
        while (true) {
            if (!automationRefreshThread) break;
            try {
                if (userID == null || ("*").equals(userID)) {
                    rs = zbdb.getCdb().selectSql("select * from biz_backpack_gift");
                    if (sqlPrint == 1) opt.addText("select * from biz_backpack_gift\n");

                } else {
                    rs = zbdb.getCdb().selectSql("select * from biz_backpack_gift where user_id = " + userID);
                    if (sqlPrint == 1) opt.addText("select * from biz_backpack_gift where user_id = " + userID + "\n");
                }
                sqlPrint++;
                ResultSetMetaData data = rs.getMetaData();
                ArrayList<String> list = new ArrayList<>();
                while (rs.next()) {
                    if (!automationRefreshThread) break;
                    StringBuffer stb = new StringBuffer();
                    for (int i = 1; i <= data.getColumnCount(); i++) {
                        //获得所有列的数目及实际列数
                        int columnCount = data.getColumnCount();
                        //获得指定列的列名
                        String columnName = data.getColumnName(i);
                        if (!"bp_gift_name".equals(columnName) &&
                                !"bp_gift_price".equals(columnName) &&
                                !"bp_gift_num".equals(columnName) &&
                                !"user_id".equals(columnName) &&
                                !"bp_gift_id".equals(columnName)) continue;
                        switch (columnName) {
                            case "bp_gift_name":
                                columnName = "name";
                                break;
                            case "bp_gift_price":
                                columnName = "price";
                                break;
                            case "bp_gift_num":
                                columnName = "num";
                                break;
                            case "bp_gift_id":
                                columnName = "id";
                                break;
                        }
                        //获得指定列的列值
                        String columnValue = rs.getString(i);
                        String str = columnName + ":" + columnValue;
                        if (!"id".equals(columnName)) str = StringUtils.leftPad(str, 15, " ");
                        stb.append(str);
                        stb = addStrBuffer(stb, "");

                    }
                    list.add(stb.toString());
                }
                ArrayList<String> resultList = new ArrayList<>();
                //检查获取的数据与历史数据是否一致，不一致的记录到recordList中
                for (int i = 0; i < list.size(); i++) {
                    boolean b = false;
                    for (String s : recordList) {
                        if (list.get(i).equals(s)) {
                            b = true;
                            continue;
                        }
                    }
                    if (!b) resultList.add(list.get(i));
                }
                recordList = list;
                //打印正在刷新
                if (resultList.size() == 0) {
                    if (sqlPrint > logPrint) {
                        opt.addText(".", false);
                        logPrint = sqlPrint + 10;
                    }

                }
                //使用迭代器打印结果
                Iterator<String> iterable = resultList.iterator();
                int index = 1;
                while (iterable.hasNext()) {
                    opt.addText(index + "：" + iterable.next() + "\n");
                    index++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                SaveCrash.save(e.toString());
                opt.setText("查询发生异常");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        opt.addText("自动刷新结束:" + WindosUtils.getDate());
        refreshClass.stop();
    }


    private StringBuffer addStrBuffer(StringBuffer stb, String text) {
        stb.append(text);
        stb.append("   ");
        return stb;
    }
}

class Refresh implements Runnable {
    private boolean stop;
    private JButton jButton;

    public Refresh(JButton jButton) {
        this.stop = false;
        this.jButton = jButton;
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        int index = 1;
        while (true) {
            String[] arr = new String[0];
            arr = ZLYUtils.FrameUtils.addFilesShiftArrays(ZLYUtils.WindosUtils.getDirectoryFilesName("image/refresh"), arr);
            if (index >= arr.length) index = 0;
            jButton.setIcon(new ImageIcon("image/refresh/" + arr[index]));
            index++;
            if (stop) break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
























