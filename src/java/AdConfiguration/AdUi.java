package AdConfiguration;

import SquirrelFrame.FrontPanel;
import SquirrelFrame.HomePage;
import SquirrelFrame.Pane;
import SquirrelFrame.SquirrelConfig;
import ZLYUtils.FrameUtils;
import org.jdatepicker.JDatePicker;
import org.jdesktop.swingx.JXDatePicker;
import com.eltima.components.ui.DatePicker;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class AdUi extends FrontPanel {


    private JButton sb;
    private JButton jButton;
    private JPanel adsJPanel;//广告位面板
    private SendAdConfiguration sendAdConfiguration;
    private List<JButton> adsList;
    private JPanel adConfigJPanel;//广告配置面板
    private JTextField adsJTextField;//已选择广告显示
    private JTextField channelidJTextField;//渠道
    private JComboBox appName;
    private JPanel jPanel;
    private GetAppType getAppType;
    private int listUpBoundary;//下拉列表上边界
    private JScrollPane jScrollPane;
    private JComboBox appType;//app类型
    private JTextField appVersion;//app版本
    public AdUi(String title) {
        super(title);
        setLayout(new GridLayout(1, 2));
        setSize(1300, 700);
        this.sendAdConfiguration = new SendAdConfiguration();
        this.adsList = new ArrayList<>();
        this.sb = newJButton(AdSendConfig.SB_JBUTTON);
        this.jScrollPane = new JScrollPane();
        this.jPanel = newJPanel();
        this.jPanel.setLayout(null);
        this.listUpBoundary = 1;
        //设置广告位面板
        setAdsJPanel();
        //设置广告配置面板
        setAdConfigJPanel();
        add(this.adConfigJPanel);
        add(this.adsJPanel);


        setVisible(true);

    }

    /**
     * 设置广告配置面板
     */
    private void setAdConfigJPanel() {
        this.adConfigJPanel = newJPanel();
        this.adConfigJPanel.setLayout(new GridLayout(20, 1));
        //设置广告位显示面板
        setAdsSoaceDisplay();
        //设置所属应用
        setAppname();
        //设置广告类型
        setAdType();
        //设置渠道
        setChannelid();
        //设置版本
        setVersion();
        //设置起始与终止时间
        setTime();
    }

    /**
     * 设置起始与终止时间
     */
    public void setTime(){
        setJPanel();
        this.jPanel.add(setJLabel("起始时间:"));
        this.jPanel.add(getDatePicker(new Date(),87,0,200,31));
        JLabel jLabel = setJLabel("终止时间:");
        Date date = new Date();
        date.setTime(date.getTime()+31536000000l);//加1年
        DatePicker datePicker = getDatePicker(date,400,0,200,31);
        this.jPanel.add(datePicker);
        jLabel.setLocation(320,this.listUpBoundary);
        this.jPanel.add(jLabel);
        this.adConfigJPanel.add(this.jPanel);
    }



    /**
     * 设置版本
     */
    public void setVersion(){
        setJPanel();
        this.jPanel.add(setJLabel("版本(分隔符\",\"):"));
        this.jPanel.add(setJScrollPane(this.appVersion = setJTextField()));
        this.adConfigJPanel.add(this.jPanel);
    }

    /**
     * 重置面板
     */
    public void setJPanel(){
        this.jPanel = newJPanel();
        this.jPanel.setLayout(null);
    }

    public JLabel setJLabel(String title){
        JLabel jLabel = newJLabel(title);
        jLabel.setLocation(AdSendConfig.LFET_MARGIN, this.listUpBoundary);
        return jLabel;
    }

    public JTextField setJTextField(){
        JTextField jTextField = newJTextField();
        jTextField.setBackground(Color.CYAN);
        return jTextField;
    }

    public JScrollPane setJScrollPane(JTextField jTextField){
        this.jScrollPane = new JScrollPane(jTextField);
        this.jScrollPane.setLocation(125, -5);
        this.jScrollPane.setSize(521, 40);
        this.jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        return this.jScrollPane;
    }
    /**
     * 设置渠道
     */
    public void setChannelid() {
        setJPanel();
        this.jPanel.add(setJLabel("渠道(分割符\",\"):"));
        this.jPanel.add(setJScrollPane(this.channelidJTextField=setJTextField()));
        this.adConfigJPanel.add(this.jPanel);
    }


    /**
     * 设置广告类型
     */
    public void setAdType() {
        this.jPanel.add(setJLabel("广告类型:"));
        this.appType = newJComboBox(new String[]{""}, 566);
        this.appType.setLocation(74, this.listUpBoundary);
        this.jPanel.add(this.appType);
        this.getAppType = new GetAppType(this);
        new Thread(this.getAppType).start();

        this.adConfigJPanel.add(jPanel);
    }

    public JComboBox getAppType() {
        return this.appType;
    }

    /**
     * 设置所属应用
     */
    private void setAppname() {
        JPanel jPanelAppname = newJPanel();
        jPanelAppname.setLayout(null);
        this.appName = newJComboBox(new String[]{
                AdSendConfig.MFDZS, AdSendConfig.MFZS
        }, 30);
        jPanelAppname.add(setJLabel("所属应用:"));
        this.appName.setOpaque(false);
        this.appName.setEditable(false);
        this.appName.setLocation(74, this.listUpBoundary);
        this.appName.setSize(120, 30);
        jPanelAppname.add(this.appName);
        this.adConfigJPanel.add(jPanelAppname);
    }

    /**
     * 设置广告面板
     */
    private void setAdsJPanel() {
        this.adsJPanel = newJPanel();
        this.adsJPanel.setLayout(new GridLayout(10, 10));
        setAds();
        this.adsJPanel.add(this.sb = newJButton("提交"));
        this.sb.setBorder(BorderFactory.createRaisedBevelBorder());
        this.sb.setBackground(Color.ORANGE);
        this.sb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.sb.setBorder(new LineBorder(
                new java.awt.Color(200, 22, 200), 5, true));
        this.adsJPanel.setLocation(getWidth() / 2 - 30, 0);
        this.adsJPanel.setSize(getWidth() / 2, getHeight());
    }

    /**
     * 设置广告位显示面板
     */
    private void setAdsSoaceDisplay() {
        JPanel adsDisplay = newJPanel();
        adsDisplay.setLayout(null);
        adsDisplay.add(setJLabel("GG:"));
        this.adsJTextField = newJTextField();
        this.adsJTextField.setEditable(false);
        this.jScrollPane = new JScrollPane(adsJTextField);
        this.jScrollPane.setLocation(35, -4);
        this.jScrollPane.setSize(620, 41);
        this.jScrollPane.setPreferredSize(new Dimension(1, 1));
        this.jScrollPane.setOpaque(false);
        this.jScrollPane.getViewport().setOpaque(false);
        this.jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        adsDisplay.add(this.jScrollPane);
        this.adConfigJPanel.add(adsDisplay);

    }

    /**
     * 设置广告位
     */
    private void setAds() {
        for (int i = 1; i < 100; i++) {
            this.jButton = newJButton(String.valueOf(i));
            this.adsList.add(this.jButton);
            this.adsJPanel.add(this.jButton);
        }
    }


    @Override
    public void setClose() {
    }

    @Override
    public void buttonClickEvent(JButton f) {
        if (this.sb == f) {
            this.sb.setBackground(Color.ORANGE);
            this.sb.setForeground(this.defaultFontColor);
            System.out.println("222");
        } else if (this.adsList.contains(f)) {
            setAds(f);
        }
    }


    @Override
    public void buttonPressEvent(JButton f) {
    }

    /**
     * 列表点击事件
     *
     * @param jComboBox
     */
    public void jComboBoxClickEvent(JComboBox jComboBox) {
        if (this.appName == jComboBox) {
            if (this.appName.getSelectedItem().toString().equals(
                    this.getAppType.getAppName()
            )) return;
            this.getAppType.setAppName(this.appName.getSelectedItem().toString());
            new Thread(this.getAppType).start();
        }
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
     * 设置广告位
     *
     * @param f
     */
    private void setAds(JButton f) {
        if (this.sendAdConfiguration.getAds().contains(Integer.parseInt(f.getText()))) {
            this.sendAdConfiguration.deleteAds(Integer.parseInt(f.getText()));
            setJButtonBackground(f, enterIntoColor);
            f.setForeground(this.defaultFontColor);
        } else {
            this.sendAdConfiguration.addAds(Integer.parseInt(f.getText()));
        }
        List<Integer> list = this.sendAdConfiguration.getAds();
        //排序
        Collections.sort(list);
        this.adsJTextField.setText(
                list.toString().
                        replace("[", "").
                        replace("]", "")
        );
    }

    public JComboBox getAppName() {
        return appName;
    }

    public void setAppName(JComboBox appName) {
        this.appName = appName;
    }

    public JButton getSb() {
        return this.sb;
    }

    public void setAppType(JComboBox appType) {
        this.appType = appType;
    }

    public static void main(String[] args) {
        new AdUi("测试");
    }
}
