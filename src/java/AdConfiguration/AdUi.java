package AdConfiguration;

import SquirrelFrame.FrontPanel;
import ZLYUtils.TooltipUtil;
import com.eltima.components.ui.DatePicker;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class AdUi extends FrontPanel {


    private JButton sb, jButton, clearJButton;
    private SendAdConfiguration sendAdConfiguration;
    private List<JButton> adsList;
    private JPanel adsJPanel, adConfigJPanel, jPanel;//广告位面板,广告配置面板,通用面板
    private JTextField adsJTextField;//已选择广告显示
    private JTextField channelidJTextField;//渠道
    private JTextField appVersion;//app版本
    private JTextField lbTime;//轮播时间
    private JTextField qz;//权重
    private JTextField singleExposureNum;//单人曝光
    private JTextField singleClickNum;//单人点击
    private JTextField totalExposureNum;//总曝光
    private JTextField totalClickNum;//总点击
    private JTextField dayTotalExposureNum;//单日曝光
    private JTextField dayTotalClickNum;//单日单击
    private JTextField displayTime;//显示时间
    private GetAppType getAppType;
    private JScrollPane jScrollPane;
    private JComboBox appName;
    private JComboBox appType;//app类型
    private JComboBox status;//上架状态
    private JComboBox wifiState;//WIFI状态
    private JComboBox builtInAppType;//内置广告类型
    private DatePicker startDate;//起始时间
    private DatePicker endDate;//结束时间
    private int listUpBoundary;//下拉列表上边界
    private int listLeftMargin;//下拉列表左边界
    private int jTextFieldLeftMargin;//输入框左边界
    private int jTextFieldYMargin;//输入框上边界
    private int jTextFieldWidth;//输入框宽度
    private int jTextFieldHight;//输入框高度
    private int appTypeWidth;//广告类型宽度
    private JRadioButton fixedTime;//固定时间
    private JRadioButton cycleDayTime;//按天循环时间


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
        this.listLeftMargin = 75;
        this.jTextFieldLeftMargin = this.listLeftMargin;
        this.jTextFieldYMargin = -3;
        this.jTextFieldWidth = 580;
        this.jTextFieldHight = 40;
        this.appTypeWidth = 566;
        //设置广告位面板
        setAdsJPanel();
        //设置广告配置面板
        setAdConfigJPanel();
        add(this.adConfigJPanel);
        add(this.adsJPanel);
        this.getAppType = new GetAppType(this);
        new Thread(this.getAppType).start();
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
        //设置时间配置策略
        setTimeConfig();
        //设置上架状态
        setStatus();
        //设置轮播时间
        setLbTime();
        //设置WIFI状态
        setWifi();
        //设置权重
        setQz();
        //设置曝光和点击
        setBgDj();
        //设置显示时间
        setDisplayTime();
        //设置内容广告类型
        setBuiltInAppType();

    }

    /**
     * 设置显示时间
     */
    public void setDisplayTime() {
        setJPanel();
        this.jPanel.add(setJLabel("显示时间:"));
        this.jPanel.add(setJScrollPane(this.displayTime = setJTextField()));
        this.displayTime.setToolTipText("默认0,仅GG-1开屏有效,单位\"s\"");
        this.displayTime.setText("0");
        this.adConfigJPanel.add(this.jPanel);
    }

    /**
     * 设置时间策略
     */
    public void setTimeConfig() {
        setJPanel();
        this.jPanel.add(setJLabel("时间策略:"));
        this.fixedTime = newJRadioButton("固定时间");
        this.fixedTime.setLocation(this.listLeftMargin, this.listUpBoundary);
        this.cycleDayTime = newJRadioButton("按天循环时间");
        this.cycleDayTime.setLocation(316, this.listUpBoundary);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.fixedTime);
        buttonGroup.add(this.cycleDayTime);
        this.fixedTime.setSelected(true);
        jRadioButtonClickEvent(this.fixedTime);//默认点击固定时间并赋值
        this.jPanel.add(this.fixedTime);
        this.jPanel.add(this.cycleDayTime);
        this.adConfigJPanel.add(this.jPanel);
    }


    /**
     * 设置内容广告类型
     */
    public void setBuiltInAppType() {
        setJPanel();
        this.jPanel.add(newJLabel("内置广告:"));
        this.jPanel.add(this.builtInAppType =
                newJComboBox(new String[]{""}, this.appTypeWidth));
        this.builtInAppType.setLocation(this.listLeftMargin, this.listUpBoundary);
        this.builtInAppType.setEnabled(false);
        this.adConfigJPanel.add(this.jPanel);
    }


    /**
     * 设置曝光和点击
     */
    public void setBgDj() {
        JTextField[] jTextFields =
                setAdLimitedNumber("单人曝光:", "单人点击:");
        this.singleExposureNum = jTextFields[0];
        this.singleExposureNum.setToolTipText("默认500");

        this.singleExposureNum.setText("500");
        this.singleClickNum = jTextFields[1];

        jTextFields = setAdLimitedNumber("总曝光    :",
                "总点击    :");
        this.totalExposureNum = jTextFields[0];
        this.totalClickNum = jTextFields[1];
        this.totalClickNum.setToolTipText("默认1000");
        this.totalClickNum.setText("10000");
        jTextFields = setAdLimitedNumber("单日曝光:",
                "单日点击:");
        this.dayTotalExposureNum = jTextFields[0];
        this.dayTotalClickNum = jTextFields[1];
        checkBgDj();

    }

    /**
     * 设置一个面板中加入两个提示和对应的文本框
     *
     * @param aStr
     * @param bStr
     * @return [0]=a,[1]=b
     */
    public JTextField[] setAdLimitedNumber(String aStr, String bStr) {
        setJPanel();
        JTextField a = setJTextField();
        JTextField b = setJTextField();
        int jScrollPaneWidth = 230;
        int jLabelX = 355;
        int jScrollPaneX = 425;
        this.jPanel.add(setJLabel(aStr));
        this.jPanel.add(setJScrollPane(a = setJTextField()));
        this.jScrollPane.setSize(jScrollPaneWidth, this.jTextFieldHight);
        JLabel jLabel = newJLabel(bStr);
        jLabel.setLocation(jLabelX, this.listUpBoundary);
        this.jPanel.add(jLabel);
        this.jPanel.add(setJScrollPane(b = setJTextField()));
        this.jScrollPane.setSize(jScrollPaneWidth, this.jTextFieldHight);
        this.jScrollPane.setLocation(jScrollPaneX, this.jTextFieldYMargin);
        this.adConfigJPanel.add(this.jPanel);
        a.setToolTipText("默认0,禁止限制请输入:0");
        b.setToolTipText("默认0,禁止限制请输入:0");
        return new JTextField[]{a, b};
    }

    public void setWifi() {
        setJPanel();
        this.jPanel.add(setJLabel("wifi开启  :"));
        this.jPanel.add(this.wifiState = setJComboBox(
                new String[]{"否", "是"}, this.listLeftMargin, 49));
        this.adConfigJPanel.add(this.jPanel);
    }

    public void setQz() {
        setJPanel();
        this.jPanel.add(setJLabel("权重占比:"));
        this.jPanel.add(setJScrollPane(this.qz = setJTextField()));
        this.qz.setText(AdSendConfig.QZ_HINT);
        this.qz.setToolTipText(AdSendConfig.QZ_HINT);
        this.adConfigJPanel.add(this.jPanel);
    }

    public void setLbTime() {
        setJPanel();
        this.jPanel.add(setJLabel("轮播时间:"));
        this.jPanel.add(setJScrollPane(this.lbTime = setJTextField()));
        this.lbTime.setText("0");
        this.lbTime.setToolTipText("默认为0,禁止轮播请输入:0,仅横幅，悬浮有效,单位\"s\"");
        this.adConfigJPanel.add(this.jPanel);
    }


    /**
     * 设置下拉列表
     *
     * @param arr
     * @param x
     * @param width
     * @return
     */
    public JComboBox setJComboBox(String[] arr, int x, int width) {
        JComboBox jComboBox = newJComboBox(arr, width);
        jComboBox.setLocation(x, this.listUpBoundary);
        jComboBox.setOpaque(false);
        jComboBox.setEditable(false);
        return jComboBox;
    }

    public void setStatus() {
        setJPanel();
        this.jPanel.add(setJLabel("是否上架:"));
        this.jPanel.add(this.status = setJComboBox(
                new String[]{"否", "是"}, this.listLeftMargin, 49));
        this.status.setSelectedIndex(1);
        this.adConfigJPanel.add(this.jPanel);

    }

    /**
     * 设置起始与终止时间
     */
    public void setTime() {
        setJPanel();
        this.jPanel.add(setJLabel("起始时间:"));
        this.jPanel.add(this.startDate = getDatePicker(new Date(), 77, 0, 200, 31));
        JLabel jLabel = setJLabel("终止时间:");
        Date date = new Date();
        date.setTime(date.getTime() + 31536000000l);//加1年
        this.endDate = getDatePicker(date, 400, 0, 200, 31);
        this.jPanel.add(this.endDate);
        jLabel.setLocation(320, this.listUpBoundary);
        this.jPanel.add(jLabel);

        this.adConfigJPanel.add(this.jPanel);
    }


    /**
     * 设置版本
     */
    public void setVersion() {
        setJPanel();
        this.jPanel.add(setJLabel("应用版本:"));
        this.jPanel.add(setJScrollPane(this.appVersion = setJTextField()));
        this.appVersion.setToolTipText("多个版本使用\",\"(支持中文\"，\")");
        this.adConfigJPanel.add(this.jPanel);
    }

    /**
     * 重置面板
     */
    public void setJPanel() {
        this.jPanel = newJPanel();
        this.jPanel.setLayout(null);
    }

    public JLabel setJLabel(String title) {
        JLabel jLabel = newJLabel(title);
        jLabel.setLocation(AdSendConfig.LFET_MARGIN, this.listUpBoundary);
        return jLabel;
    }

    public JTextField setJTextField() {
        JTextField jTextField = newJTextField();
        return jTextField;
    }

    public JScrollPane setJScrollPane(JTextField jTextField) {
        this.jScrollPane = new JScrollPane(jTextField);
        this.jScrollPane.setLocation(this.jTextFieldLeftMargin, this.jTextFieldYMargin);
        this.jScrollPane.setSize(this.jTextFieldWidth, this.jTextFieldHight);
        this.jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        return this.jScrollPane;
    }

    /**
     * 设置渠道
     */
    public void setChannelid() {
        setJPanel();
        this.jPanel.add(setJLabel("所属渠道:"));
        this.jPanel.add(setJScrollPane(this.channelidJTextField = setJTextField()));
        this.channelidJTextField.setToolTipText("多个渠道使用\",\"(支持中文\"，\")");
        this.adConfigJPanel.add(this.jPanel);
    }


    /**
     * 设置广告类型
     */
    public void setAdType() {
        setJPanel();
        this.jPanel.add(setJLabel("广告类型:"));
        this.appType = newJComboBox(new String[]{""}, this.appTypeWidth);
        this.appType.setLocation(this.listLeftMargin, this.listUpBoundary);
        this.jPanel.add(this.appType);
        this.adConfigJPanel.add(this.jPanel);
    }

    public JComboBox getAppType() {
        return this.appType;
    }

    /**
     * 设置所属应用
     */
    private void setAppname() {
        setJPanel();
        this.jPanel.add(setJLabel("所属应用:"));
        this.jPanel.add(this.appName = setJComboBox(
                new String[]{
                        AdSendConfig.MFDZS, AdSendConfig.MFZS}, this.listLeftMargin, 120));
        this.adConfigJPanel.add(this.jPanel);
    }

    /**
     * 设置广告面板
     */
    private void setAdsJPanel() {
        this.adsJPanel = newJPanel();
        this.adsJPanel.setLayout(new GridLayout(10, 10));
        this.clearJButton = newJButton("clear");
        this.clearJButton.setToolTipText("清空广告位");
        this.clearJButton.setBorder(newLineBorder());
        this.adsJPanel.add(this.clearJButton);
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
        setJPanel();
        this.jPanel.add(setJLabel("GG:"));
        this.adsJTextField = newJTextField();
        this.adsJTextField.setEditable(false);
        this.jScrollPane = new JScrollPane(adsJTextField);
        this.adsJTextField.setToolTipText("不支持输入,请从右侧选取");
        this.jScrollPane.setLocation(35, -4);
        this.jScrollPane.setSize(610, 41);
        this.jScrollPane.setPreferredSize(new Dimension(1, 1));
        this.jScrollPane.setOpaque(false);
        this.jScrollPane.getViewport().setOpaque(false);
        this.jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        this.jPanel.add(this.jScrollPane);
        this.adConfigJPanel.add(this.jPanel);


    }

    /**
     * 设置广告位
     */
    private void setAds() {
        for (int i = 1; i < 99; i++) {
            this.jButton = newJButton(String.valueOf(i));
            this.adsList.add(this.jButton);
            this.adsJPanel.add(this.jButton);
        }
    }


    @Override
    public void setClose() {
    }

    @Override
    public void jRadioButtonClickEvent(JRadioButton jRadioButton) {
        if (this.fixedTime == jRadioButton) {
            this.sendAdConfiguration.setIscirclead((byte) 0);
        } else if (this.cycleDayTime == jRadioButton) {
            this.sendAdConfiguration.setIscirclead((byte) 1);
        }
    }

    @Override
    public void jTextFieldEnteredEvent(JTextField jTextField) {
        if (jTextField == this.qz) {
            if (this.qz.getText().equals(AdSendConfig.QZ_HINT)) jTextField.setText("");
        }
    }

    @Override
    public void jTextFieldReleasedEvent(JTextField jTextField) {
    }

    @Override
    public void jTextFieldExitedEvent(JTextField jTextField) {

    }

    @Override
    public void jTextFieldInputEvent(JTextField jTextField, KeyEvent e) {
        if (jTextField != this.appVersion &&
                jTextField != this.channelidJTextField) {
            String text = jTextField.getText();
            if (text.length() > 9) jTextField.setText(
                    text.substring(0, 10));
            int keyChar = e.getKeyChar();
            //限制输入非数字字符
            if (keyChar < 48 || keyChar > 57) {
                e.consume();
            }
            //限制输入空格
            if (KeyEvent.VK_SPACE == keyChar) {
                e.consume();
            }
            setBgDjRestrict(jTextField, e);
        }

    }


    @Override
    public void jTextFieldPressedEvent(JTextField jTextField) {
        if (jTextField != this.appVersion ||
                jTextField == this.channelidJTextField) {
            if (jTextField.getText().equals("0")) jTextField.setText("");
        }
    }

    /**
     * 制约曝光和点击
     *
     * @param jTextField
     */
    public void setBgDjRestrict(JTextField jTextField, KeyEvent e) {
        if ((int) e.getKeyChar() == 48) return;
        if (this.totalExposureNum == jTextField) {
            this.totalClickNum.setText("0");
            this.dayTotalExposureNum.setText("0");
            this.dayTotalClickNum.setText("0");
        } else if (this.totalClickNum == jTextField) {
            this.totalExposureNum.setText("0");
            this.dayTotalExposureNum.setText("0");
            this.dayTotalClickNum.setText("0");
        } else if (this.singleExposureNum == jTextField) {
            this.singleClickNum.setText("0");
            this.dayTotalExposureNum.setText("0");
            this.dayTotalClickNum.setText("0");
        } else if (this.singleClickNum == jTextField) {
            this.singleExposureNum.setText("0");
            this.dayTotalExposureNum.setText("0");
            this.dayTotalClickNum.setText("0");
        } else if (this.dayTotalExposureNum == jTextField) {
            this.dayTotalClickNum.setText("0");
            this.totalClickNum.setText("0");
            this.totalExposureNum.setText("0");
        } else if (this.dayTotalClickNum == jTextField) {
            this.dayTotalExposureNum.setText("0");
            this.totalClickNum.setText("0");
            this.totalExposureNum.setText("0");
        }
    }

    @Override
    public void jTextFieldClickEvent(JTextField jTextField) {

    }


    @Override
    public void buttonClickEvent(JButton f) {
        if (this.sb == f) {
            this.sb.setEnabled(false);
            try {
                checkValues();
                this.sendAdConfiguration.setAppname(
                        AdSendConfig.getAppNameCode(this.appName.getSelectedItem().toString()));
                this.sendAdConfiguration.setAdNo(Long.parseLong(
                        this.getAppType.getAdNoMap().get(
                                this.appType.getSelectedItem().toString())));
                this.sendAdConfiguration.setSb(this.sb.getText());
                this.sendAdConfiguration.setChannelid(analysisSeparator(this.channelidJTextField.getText()));
                this.sendAdConfiguration.setVersion(analysisSeparator(this.appVersion.getText()));
                this.sendAdConfiguration.setRelStartDate(this.startDate.getText());
                this.sendAdConfiguration.setRelEndDate(this.endDate.getText());
                this.sendAdConfiguration.setStatus((byte) (this.status.getSelectedIndex()));
                this.sendAdConfiguration.setTkTime(Integer.parseInt(this.displayTime.getText()));
                this.sendAdConfiguration.setLbtime(Integer.parseInt(this.lbTime.getText()));
                this.sendAdConfiguration.setWifiState((byte) (this.wifiState.getSelectedIndex()));
                this.sendAdConfiguration.setSingleClickNum(Integer.parseInt(this.singleClickNum.getText()));
                this.sendAdConfiguration.setSingleExposureNum(Integer.parseInt(this.singleExposureNum.getText()));
                this.sendAdConfiguration.setTotalClickNum(Integer.parseInt(this.totalClickNum.getText()));
                this.sendAdConfiguration.setTotalExposureNum(Integer.parseInt(this.totalExposureNum.getText()));
                this.sendAdConfiguration.setDayTotalClickNum(Integer.parseInt(this.dayTotalClickNum.getText()));
                this.sendAdConfiguration.setDayTotalExposureNum(Integer.parseInt(this.dayTotalExposureNum.getText()));
                if (this.qz.getText().equals(AdSendConfig.QZ_HINT)) {
                    this.sendAdConfiguration.setQz(10);
                } else {
                    this.sendAdConfiguration.setQz(Integer.parseInt(this.qz.getText()));
                }

                this.sendAdConfiguration.sendAd();
            } catch (Exception e) {
                e.printStackTrace();
                TooltipUtil.errTooltip(e.toString());
            }
            this.sb.setBackground(Color.ORANGE);
            this.sb.setForeground(this.defaultFontColor);
            this.sb.setEnabled(true);

        } else if (this.adsList.contains(f)) {
            setAds(f);
        } else if (this.clearJButton == f) {

            this.clearJButton.setBackground(Color.pink);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    clearAds();
                }
            }).start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.clearJButton.setBackground(this.enterIntoColor);
            this.clearJButton.setForeground(this.defaultFontColor);
        }
    }

    /**
     * 检查参数合法性
     */
    public void checkValues() {
        if (this.qz.getText().equals("")) this.qz.setText("10");
        if (this.lbTime.getText().equals("")) this.lbTime.setText("0");
        if (this.displayTime.getText().equals("")) this.displayTime.setText("0");
        checkBgDj();
    }

    /**
     * 检查曝光和点击合法性
     */
    public void checkBgDj() {
        setBgDjDefault(this.dayTotalClickNum);
        setBgDjDefault(this.dayTotalExposureNum);
        setBgDjDefault(this.singleClickNum);
        setBgDjDefault(this.singleExposureNum);
        setBgDjDefault(this.totalExposureNum);
        setBgDjDefault(this.totalClickNum);
        if (Integer.parseInt(this.singleClickNum.getText()) >
                Integer.parseInt(this.totalClickNum.getText())) {
            if (Integer.parseInt(this.totalClickNum.getText()) != 0)
                throw new IllegalArgumentException("单人点击不能大于总点击");
        } else if (Integer.parseInt(this.singleExposureNum.getText()) >
                Integer.parseInt(this.totalExposureNum.getText())) {
            if (Integer.parseInt(this.totalExposureNum.getText()) != 0)
                throw new IllegalArgumentException("单人曝光不能大于总曝光");
        }
    }

    /**
     * 设置曝光和点击默认值
     */
    public void setBgDjDefault(JTextField jTextField) {
        if (jTextField.getText().equals("")) jTextField.setText("0");
    }


    /**
     * 解析分割符"，"
     *
     * @return
     */
    public List<String> analysisSeparator(String str) {
        if (str == null) throw new IllegalArgumentException("字符串为空");
        List<String> list = new ArrayList<>();
        if (str.indexOf(",") == -1) {
            list.add(str);
        } else {
            str = str.replace("，", ",");
            for (String s : str.split(",")) {
                s = s.trim();
                list.add(s);
            }
        }
        return list;
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
        } else if (this.builtInAppType == jComboBox) {
            String text = this.builtInAppType.getSelectedItem().toString();
            for (int i = 0; i < this.getAppType.getVector().size(); i++) {
                if (text.equals(this.getAppType.getVector().get(i))) {
                    this.appType.setSelectedIndex(i);
                    setAppBuiltInAds(this.appType.getSelectedItem().toString());
                    break;
                }
            }
            if (text.equals(AdSendConfig.ZHI_TOU)) {
                this.appType.setSelectedIndex(0);
                setAppBuiltInAds(AdSendConfig.ZHI_TOU);
            }

        }
    }

    /**
     * 设置内置广告位
     */

    public void setAppBuiltInAds(String appType) {
        clearAds();
        for (Integer i : AdSendConfig.builtInAdGG(
                this.appName.getSelectedItem().toString(), appType)) {
            this.sendAdConfiguration.addAds(i);
            setJButtonClickColor(this.adsList.get(i - 1));
        }
        setAdsGG();
    }

    /**
     * 清空广告位
     */
    public void clearAds() {
        for (JButton j : this.adsList) {
            if (this.sendAdConfiguration.getAds().size() == 0) break;
            this.sendAdConfiguration.deleteAds(Integer.parseInt(j.getText()));
            setJButtonBackground(j, this.defaultColor);
            j.setForeground(this.defaultFontColor);
        }
        this.adsJTextField.setText("");
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
        setAdsGG();
    }

    /**
     * 设置广告位显示广告
     */
    public void setAdsGG() {
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

    public JComboBox getBuiltInAppType() {
        return builtInAppType;
    }

    public static void main(String[] args) {
        new AdUi("测试");
    }

    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    AdSendConfig.loging();
                    try {
                        Thread.sleep(1000 * 5 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
