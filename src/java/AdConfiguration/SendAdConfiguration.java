package AdConfiguration;

import ZLYUtils.Network;
import ZLYUtils.NetworkHeaders;
import ZLYUtils.TooltipUtil;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 发送Ad配置
 */
public class SendAdConfiguration {
    private List<Integer> ads;//广告位
    private int dayTotalClickNum;//单天总点击机
    private int singleExposureNum;//单人曝光
    private int totalClickNum;//总点击
    private int totalExposureNum;//总曝光
    private int dayTotalExposureNum;//单天总曝光
    private int singleClickNum;//单人点击
    private String relStartDate;//起始时间
    private String relEndDate;//终止时间
    private List<String> version;//版本
    private List<String> channelid;//渠道
    private String appname;//APP名称
    private int qz;//权重
    private byte status;//状态
    private byte iscirclead;//时间配置策略  0:固定时间   1:按天循环时间
    private int lbtime;//轮播时间
    private byte wifiState;//WIFI状态 0:否  1:是
    private Long adNo;//广告类型对应的编号
    private int tkTime; //显示时间GG-1
    private int cycpNum; //插页间隔张数
    private int sxdisNum; //书型广告位置
    private int isSpecial; //是否特殊配置
    private String sb;
    private String separator;
    StringBuffer urlValue;//请求参数
    private NetworkHeaders networkHeaders;
    private String repetition;//是否是重复数据
    private int channelidShelves;//渠道下架
    private int versionShelves;//版本下架
    private int timeShelves;//时间下架
    private int lbtimeShelves;//轮播下架
    private int wifiShelves;//wifi下架
    private int bgdjShelves;//曝光点击下架
    private String createDate;//创建时间

    public SendAdConfiguration() {
        this.ads = new ArrayList<>();//广告位
        this.dayTotalClickNum = 0;//单天总单机
        this.singleExposureNum = 0;//单人曝光
        this.totalClickNum = 0;//总点击
        this.totalExposureNum = 0;//总曝光
        this.dayTotalExposureNum = 0;//单天总曝光
        this.singleClickNum = 0;//单人点击
        this.relStartDate = null;//起始时间
        this.relEndDate = null;//终止时间
        this.version = new ArrayList<>();//版本
        this.channelid = new ArrayList<>();//渠道
        this.appname = null;//APP名称
        this.qz = 10;//权重
        this.status = 0;//状态
        this.iscirclead = 0;//时间配置策略  0:固定时间   1:按天循环时间
        this.lbtime = 0;//轮播时间
        this.wifiState = 0;//WIFI状态 0:否  1:是
        this.urlValue = new StringBuffer();
        this.adNo = 0l;
        this.tkTime = 0;
        this.cycpNum = 0;
        this.sxdisNum = 0;
        this.isSpecial = 0;
        this.sb = "";
        this.separator = ",";
        this.networkHeaders = new NetworkHeaders();
        this.repetition = "";
        this.channelidShelves = -1;
        this.versionShelves = -1;
        this.timeShelves = -1;
        this.lbtimeShelves = -1;
        this.wifiShelves = -1;
        this.bgdjShelves = -1;
        this.createDate = null;

    }

    /**
     * 发送更新状态数据库
     *
     * @return
     */
    public int sendUpdateStaus() {
        return AdDataBse.getAdDataBse().updateStatusOnTheShelf(
                this.appname, jointValues(this.version, this.separator),
                jointValues(this.channelid, this.separator),
                this.relStartDate, this.relEndDate, this.qz, this.iscirclead,
                this.lbtime, this.dayTotalClickNum, this.dayTotalExposureNum,
                this.totalClickNum, this.totalExposureNum, this.singleClickNum,
                this.singleExposureNum, this.adNo, this.ads, this.wifiState
        );
    }

    /**
     * 检查参数
     */
    public void checkValues(boolean shelves) {
        if (!shelves) {
            if (this.ads.size() == 0 ||
                    this.ads.toString().equals("[]")) throw new IllegalArgumentException("广告位为空");
            if (this.adNo == 0l) throw new IllegalArgumentException("广告类型adNo未选择");
        }
        if (this.relStartDate == null) throw new IllegalArgumentException("起始时间为空");
        if (this.relEndDate == null) throw new IllegalArgumentException("结束时间为空");
        if (this.version.size() == 0 ||
                this.version.toString().equals("[]")) throw new IllegalArgumentException("版本为空");
        if (this.channelid.size() == 0 ||
                this.channelid.toString().equals("[]")) throw new IllegalArgumentException("渠道为空");
        if (this.appname == null) throw new IllegalArgumentException("APP名称为空");

        if (!this.relStartDate.matches(
                "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"))
            throw new IllegalArgumentException("relStartDate参数不正确:" + this.relStartDate);
        if (!this.relEndDate.matches(
                "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"))
            throw new IllegalArgumentException("relEndDate参数不正确:" + this.relEndDate);
        if (this.sb.equals("")) throw new IllegalArgumentException("sb参数不正确:" + this.sb);
    }

    /**
     * 发送下架
     *
     * @return
     */
    public int sendShelves() {
        checkValues(true);
        if (this.channelidShelves == -1) throw new IllegalArgumentException("渠道下架未配置");
        if (this.versionShelves == -1) throw new IllegalArgumentException("版本下架未配置");
        if (this.timeShelves == -1) throw new IllegalArgumentException("时间下架未配置");
        if (this.lbtimeShelves == -1) throw new IllegalArgumentException("轮播时间下架未配置");
        if (this.wifiShelves == -1) throw new IllegalArgumentException("wifi下架未配置");
        if (this.bgdjShelves == -1) throw new IllegalArgumentException("曝光点击下架未配置");
        return AdDataBse.getAdDataBse().updateShelves(
                this.appname, jointValues(this.version, this.separator),
                jointValues(this.channelid, this.separator),
                this.relStartDate, this.relEndDate, this.qz, this.iscirclead,
                this.lbtime, this.dayTotalClickNum, this.dayTotalExposureNum,
                this.totalClickNum, this.totalExposureNum, this.singleClickNum,
                this.singleExposureNum, this.adNo, this.ads, this.wifiState,
                this.channelidShelves, this.versionShelves, this.timeShelves,
                this.lbtimeShelves, this.wifiShelves, this.bgdjShelves
        );
    }

    /**
     * 发送广告
     *
     * @return true 发送成功
     */
    public boolean sendAd() throws IllegalArgumentException {
        checkValues(false);
        this.urlValue.delete(0, this.urlValue.length());
        //拼接事件
        setSel();
        stitchingParameters(AdSendConfig.VERSION, jointValues(this.version, this.separator));
        stitchingParameters(AdSendConfig.CHANNELID, jointValues(this.channelid, this.separator));
        stitchingParameters(AdSendConfig.CURRENT_PAGE, "1");
        stitchingParameters(AdSendConfig.APPNAME, this.appname);
        stitchingParameters(AdSendConfig.AD_NO, this.adNo);
        for (int advSingNo : this.ads) {
            stitchingParameters(AdSendConfig.ADV_SING_NO, "GG-" + advSingNo);
        }
        stitchingParameters(AdSendConfig.RES_START_DATE,
                ZLYUtils.Network.getEncoderString(this.relStartDate, AdSendConfig.ENCODER));
        stitchingParameters(AdSendConfig.REL_END_DATE,
                ZLYUtils.Network.getEncoderString(this.relEndDate, AdSendConfig.ENCODER));
        stitchingParameters(AdSendConfig.IS_CIRCLEAD, this.iscirclead);
        stitchingParameters(AdSendConfig.QZ, this.qz);
        stitchingParameters(AdSendConfig.TK_TIME, this.tkTime);
        stitchingParameters(AdSendConfig.CYCP_NUM, this.cycpNum);
        stitchingParameters(AdSendConfig.LB_TIME, this.lbtime);
        stitchingParameters(AdSendConfig.SXDIS_NUM, this.sxdisNum);
        stitchingParameters(AdSendConfig.IS_SPECIAL, this.isSpecial);
        stitchingParameters(AdSendConfig.WIFI_STATE, this.wifiState);
        stitchingParameters(AdSendConfig.STATUS, this.status);
        stitchingParameters(AdSendConfig.SB,
                ZLYUtils.Network.getEncoderString(this.sb, AdSendConfig.ENCODER));
        setBgDj();
        String urlValues = this.urlValue.toString();
        if (this.repetition.equals(urlValues)) {
            if (TooltipUtil.yesNoTooltip("数据已提交，是否重复提交?") == 1) {
                return false;
            } else {
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                urlValues = urlValues.replace(ZLYUtils.Network.getEncoderString(this.relStartDate, AdSendConfig.ENCODER),
                        time);
                this.repetition = this.urlValue.toString();
                this.relStartDate = time;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TooltipUtil.generalTooltip("起始时间更改为:" + time);
                    }
                }).start();
            }
        } else {
            this.repetition = this.urlValue.toString();
        }

        String response = Network.sendPost(AdSendConfig.getHostUrl() + AdSendConfig.ADD_AD_RELEASE,
                urlValues,
                AdSendConfig.HEADERS, this.networkHeaders);
        if (this.networkHeaders.getHeaders().get("Location").toString().contains(
                AdSendConfig.getHostUrl() + "/fm/listadrelease"
        ) && response.equals("302")) return true;
        return false;
    }

    private void setBgDj() {
        stitchingParameters(AdSendConfig.SINGLE_CLICK_NUM, this.singleClickNum);
        stitchingParameters(AdSendConfig.SINGLE_EXPOSURE_NUM, this.singleExposureNum);
        stitchingParameters(AdSendConfig.DAY_TOTAL_CLICK_NUM, this.dayTotalClickNum);
        stitchingParameters(AdSendConfig.DAY_TOTAL_EXPOSURE_NUM, this.dayTotalExposureNum);
        stitchingParameters(AdSendConfig.TOTAL_CLICK_NUM, this.totalClickNum);
        stitchingParameters(AdSendConfig.TOTAL_EXPOSURE_NUM, this.totalExposureNum);
    }


    /**
     * 设置事件
     *
     * @return
     */
    private void setSel() {
        stitchingParameters(AdSendConfig.SEL_APPNAME, "cxb");
        stitchingParameters(AdSendConfig.SEL_VERSION, "");
        stitchingParameters(AdSendConfig.SEL_CHANNELID, "");
        stitchingParameters(AdSendConfig.SEL_AD_NO, "");
        stitchingParameters(AdSendConfig.SEL_STATUS, "-1");
        stitchingParameters(AdSendConfig.RDO_SINGLE, "0");
        stitchingParameters(AdSendConfig.RDO_TOTAL, "0");
    }

    /**
     * 拼接值，分隔符
     *
     * @param list
     * @param separator 分隔符
     * @return
     */
    private synchronized String jointValues(List<String> list, String separator) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Object s : list) {
            stringBuffer.append(s.toString());
            stringBuffer.append(separator);
        }
        return stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length()).toString();
    }


    /**
     * 拼接参数
     */
    private synchronized void stitchingParameters(String key, Object value) {
        if (!this.urlValue.toString().endsWith("&") &&
                !this.urlValue.toString().equals("")) {
            this.urlValue.append("&");
        }
        this.urlValue.append(key);
        this.urlValue.append("=");
        this.urlValue.append(String.valueOf(value));

    }


    public List<Integer> getAds() {
        return ads;
    }

    public synchronized void setAds(List<Integer> ads) {
        this.ads = ads;
    }

    public synchronized void addAds(int ads) {

        this.ads.add(ads);
    }

    public synchronized boolean deleteAds(Integer ads) {
        return this.ads.remove(ads);
    }

    public int getDayTotalClickNum() {
        return dayTotalClickNum;
    }

    public void setDayTotalClickNum(int dayTotalClickNum) {
        this.dayTotalClickNum = dayTotalClickNum;
    }


    public int getSingleExposureNum() {
        return singleExposureNum;
    }

    public void setSingleExposureNum(int singleExposureNum) {
        this.singleExposureNum = singleExposureNum;
    }

    public int getTotalClickNum() {
        return totalClickNum;
    }

    public void setTotalClickNum(int totalClickNum) {
        this.totalClickNum = totalClickNum;
    }

    public int getTotalExposureNum() {
        return totalExposureNum;
    }

    public void setTotalExposureNum(int totalExposureNum) {
        this.totalExposureNum = totalExposureNum;
    }

    public int getDayTotalExposureNum() {
        return dayTotalExposureNum;
    }

    public void setDayTotalExposureNum(int dayTotalExposureNum) {
        this.dayTotalExposureNum = dayTotalExposureNum;
    }

    public int getSingleClickNum() {
        return singleClickNum;
    }

    public void setSingleClickNum(int singleClickNum) {
        this.singleClickNum = singleClickNum;
    }

    public String getRelStartDate() {
        return relStartDate;
    }

    public void setRelStartDate(String relStartDate) {
        this.relStartDate = relStartDate;
    }

    public String getRelEndDate() {
        return relEndDate;
    }

    public void setRelEndDate(String relEndDate) {
        this.relEndDate = relEndDate;
    }

    public List<String> getVersion() {
        return version;
    }

    public void setVersion(List<String> version) {
        this.version = version;
    }

    public void addVersion(String version) {
        this.version.add(version);
    }


    public List<String> getChannelid() {
        return channelid;
    }

    public void setChannelid(List<String> channelid) {
        this.channelid = channelid;
    }

    public void addChannelid(String channelid) {
        this.channelid.add(channelid);
    }


    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public int getQz() {
        return qz;
    }

    public void setQz(int qz) {
        this.qz = qz;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getIscirclead() {
        return iscirclead;
    }

    public void setIscirclead(byte iscirclead) {
        this.iscirclead = iscirclead;
    }

    public int getLbtime() {
        return lbtime;
    }

    public void setLbtime(int lbtime) {
        this.lbtime = lbtime;
    }

    public byte getWifiState() {
        return wifiState;
    }

    public void setWifiState(byte wifiState) {
        this.wifiState = wifiState;
    }

    public Long getAdNo() {
        return adNo;
    }

    public void setAdNo(Long adNo) {
        this.adNo = adNo;
    }

    public String getSb() {
        return sb;
    }

    public void setSb(String sb) {
        this.sb = sb;
    }

    public void setTkTime(int tkTime) {
        this.tkTime = tkTime;
    }


    public void setChannelidShelves(int channelidShelves) {
        this.channelidShelves = channelidShelves;
    }

    public void setVersionShelves(int versionShelves) {
        this.versionShelves = versionShelves;
    }

    public void setTimeShelves(int timeShelves) {
        this.timeShelves = timeShelves;
    }

    public void setLbtimeShelves(int lbtimeShelves) {
        this.lbtimeShelves = lbtimeShelves;
    }

    public void setWifiShelves(int wifiShelves) {
        this.wifiShelves = wifiShelves;
    }

    public void setBgdjShelves(int bgdjShelves) {
        this.bgdjShelves = bgdjShelves;
    }

    public static void main(String[] args) {
        SendAdConfiguration sendAdConfiguration = new SendAdConfiguration();
        sendAdConfiguration.setRelStartDate("2018-05-05 58:45:45");
        sendAdConfiguration.setRelEndDate("2018-05-05 58:45:45");
        sendAdConfiguration.setAppname("cxb");
        sendAdConfiguration.setAdNo(222l);
        sendAdConfiguration.setSb("提交");
        System.out.println(sendAdConfiguration.sendAd());
    }
}
