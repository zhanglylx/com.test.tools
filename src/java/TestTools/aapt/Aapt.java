package TestTools.aapt;

import Frame.FrontPanel;
import ZLYUtils.AaptUtils;
import ZLYUtils.JavaUtils;
import ZLYUtils.SwingUtils;
import ZLYUtils.TooltipUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;
import java.util.List;

/**
 * aaptUi工具页面
 */
public class Aapt extends FrontPanel {
    //    获取权限按钮
    private JButton permissionsJButton;
    //获取包名与activity
    private JButton activityJButton;
    //    显示内容
    private JTextArea jTextAreaDisplay;
    //    过滤器
    private JTextField jTextFieldFilter;
    private String filterDefaultValue = "请输入过滤条件，多个条件使用 \";\" 支持中文分号,不区分大小写,getPermissions默认3个过滤：SMS;RECORD;TASKS";
    //过滤器滚动窗口
    private JScrollPane jScrollPaneJTextFieldFilter;
    //显示过滤内容
    private JTable jTableFilterShow;

    public Aapt(String title) {
        super(title, false, true);
        this.setLocation(100, 10);
        this.setSize(820, 700);
//        this.setLayout(new GridLayout(1,1));
        this.setLayout(null);
        //    功能区
        JPanel functionalJPanel = newJPanel(true);
        functionalJPanel.setSize(150, this.getHeight() - 30);
        functionalJPanel.add(this.permissionsJButton = newJButton("getPermissions"));
        this.permissionsJButton.setToolTipText("请选择一个apk文件");
        functionalJPanel.add(this.activityJButton = newJButton("get包名/入口", "请选择一个apk文件"));
        // 表头（列名）
        Vector columnNames = new Vector();
        columnNames.add("过滤器展示");

// 表格所有行数据
        this.jTableFilterShow = newJTable(columnNames, 20);
        JScrollPane jTableJScrollPane = newJScrollPane(this.jTableFilterShow
                , functionalJPanel.getWidth() - 10
                , 20);
        functionalJPanel.add(jTableJScrollPane);
        add(functionalJPanel);
        this.jTextAreaDisplay = newJTextArea();
        this.jTextAreaDisplay.setLineWrap(false);
        JScrollPane jScrollPaneJTextAreaDisplay = newJScrollPane(this.jTextAreaDisplay);
        jScrollPaneJTextAreaDisplay.setSize(this.getWidth() - functionalJPanel.getWidth() - 7
                , functionalJPanel.getHeight() - 60);
        jScrollPaneJTextAreaDisplay.setLocation(functionalJPanel.getX() + functionalJPanel.getWidth()
                , functionalJPanel.getY() + 60);
        add(jScrollPaneJTextAreaDisplay);
        this.jTextFieldFilter = newJTextField();
        this.jTextFieldFilter.setText(this.filterDefaultValue);
        this.jScrollPaneJTextFieldFilter = newJScrollPane(this.jTextFieldFilter);
        this.jScrollPaneJTextFieldFilter.setSize(jScrollPaneJTextAreaDisplay.getWidth(), 60);
        this.jScrollPaneJTextFieldFilter.setLocation(jScrollPaneJTextAreaDisplay.getX(), 0);
        add(this.jScrollPaneJTextFieldFilter);
        this.setVisible(true);
    }


    @Override
    public int setClose() {
        return 2;
    }

    @Override
    public void jRadioButtonClickEvent(JRadioButton jRadioButton) {

    }

    @Override
    public void jTextFieldEnteredEvent(JTextField jTextField) {
        if (this.jTextFieldFilter == jTextField) {
            if (this.filterDefaultValue.equals(jTextField.getText())) {
                this.jTextFieldFilter.setText("");
            }
        }
    }

    @Override
    public void jTextFieldReleasedEvent(JTextField jTextField) {
    }

    @Override
    public void jTextFieldExitedEvent(JTextField jTextField) {
        if (this.jTextFieldFilter == jTextField) {
            if (this.jTextFieldFilter.getText().trim().equals("")) {
                this.jTextFieldFilter.setText(this.filterDefaultValue);
            }
        }
    }

    @Override
    public void jTextFieldInputEvent(JTextField jTextField, KeyEvent e) {
        if (this.jTextFieldFilter == jTextField) {
            SwingUtils.setJScrollPaneBarRight(this.jScrollPaneJTextFieldFilter, this.jTextFieldFilter);
        }

    }

    @Override
    public void jTextFieldPressedEvent(JTextField jTextField) {

    }

    @Override
    public void jTextFieldClickEvent(JTextField jTextField) {

    }

    @Override
    public void buttonClickEvent(JButton jButton) {
        if (this.permissionsJButton == jButton) {
            this.jTextFieldFilter.setText(this.jTextFieldFilter.getText());
            executeGetPermissions();
        }
        SwingUtils.setJTableData(this.jTableFilterShow, getFilters());
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

    /**
     * 执行获取权限
     */
    private void executeGetPermissions() {
        try {
            File filePath = SwingUtils.selectApkFile(this);
            if (null == filePath) return;
            showContent(filePath, AaptUtils.runAapt("dunmp permissions " + filePath));

        } catch (Exception e) {
            TooltipUtil.errTooltip(e.toString());
        }
    }

    /**
     * 显示内容
     */
    public void showContent(File filePath, List<String> permissions) {
        try {
            this.jTextAreaDisplay.setText("");
            this.jTextAreaDisplay.setText(filePath.getName() + " 共获取到[" + permissions.size() + "]条记录\n");
            int i = 0;
            for (String str : permissions) {
                if (filterJTextAreaDisplayText(str)) {
                    i++;
                    this.jTextAreaDisplay.append(i + ":" + str + "\r\n");
                }
            }
            this.jTextAreaDisplay.append("\n");
            this.jTextAreaDisplay.append(filePath.getName() + " 共筛选出[" + i + "]条记录\n");
            this.jTextAreaDisplay.append("\n所有记录：\n");
            i = 1;
            for (String str : permissions) {
                this.jTextAreaDisplay.append(i + ":" + str + "\r\n");
                i++;
            }

        } catch (Exception e) {
            TooltipUtil.errTooltip(e.toString());
        }
    }

    /**
     * 判断字符串是否在过滤器中
     *
     * @param text
     * @return
     */
    private boolean filterJTextAreaDisplayText(String text) {
        Vector[] vectors = getFilters();
        if (vectors.length == 0) return true;
        for (Vector filter : getFilters()) {
            for (Object o : filter) {
                if (text.trim().toLowerCase().contains(o.toString().trim().toLowerCase())) return true;
            }

        }
        return false;
    }


    private Vector[] getFilters() {
        Vector vector;
        String text = this.jTextFieldFilter.getText().trim();
        String[] filters = new String[]{};
        if (text.equals(this.filterDefaultValue)) {
//            if (StringUtils.isBlank(text) ||
//                    text.equals(this.filterDefaultValue)) return filters;
            filters = new String[]{"SMS", "RECORD", "TASKS"};
        } else {
            filters = text.replaceAll("；", ";").split(";");
            filters = JavaUtils.arraysLenthAdd(filters, "SMS", "RECORD", "TASKS");
        }
        Vector[] vectors = new Vector[filters.length];

        for (int i = 0; i < filters.length; i++) {
            vector = new Vector();
            vector.add(filters[i]);
            vectors[i] = vector;
        }
        return vectors;
    }


    public static void main(String[] args) {
        new Aapt("test");
    }
}
