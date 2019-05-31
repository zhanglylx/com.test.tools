package TestTools.ad_configuration.DataBase;

import ZLYUtils.JavaUtils;
import ZLYUtils.WindosUtils;

public class FreeadReslease {
    private String appname;
    private String advSingNo;
    private String adNo;
    private String channelid;
    private String version;
    private String relStartDate;
    private String relEndDate;
    private int qz;
    private int lbtime;
    private int tktime;
    private int cycpnum;
    private int sxdisnum;
    private int status;
    private int isdel;
    private String updateDate;
    private String createDate;
    private int isspecial;
    private int iscirclead;
    private long singleClickNum;
    private long singleExposureNum;
    private long totalClickNum;
    private long totalExposureNum;
    private int wifiState;
    private long dayTotalClickNum;
    private long dayTotalExoisureNum;
    private int cyynum;
    private int videoAdSet;
    private int videoAdRate;
    private int bookLocation;
    private int startCha;
    private int shieldAdTime;
    private int adStatus;
    private int netState;
    public FreeadReslease(){
        this.tktime=0;
        this.cycpnum=0;
        this.sxdisnum=0;
        this.isdel=0;
        this.updateDate= WindosUtils.getDate();
        this.createDate=WindosUtils.getDate();
        this.isspecial=0;
        this.iscirclead=0;
        this.singleClickNum=0;
        this.singleExposureNum=0;
        this.totalClickNum=0;
        this.totalExposureNum=0;
        this.dayTotalClickNum=0;
        this.dayTotalExoisureNum=0;
        this.cyynum=0;
        this.videoAdSet=0;
        this.videoAdRate=0;
        this.bookLocation=0;
        this.startCha=0;
        this.shieldAdTime=0;
        this.adStatus=0;
        this.netState=0;
    }


    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAdvSingNo() {
        return advSingNo;
    }

    public void setAdvSingNo(String advSingNo) {
        this.advSingNo = advSingNo;
    }

    public String getAdNo() {
        return adNo;
    }

    public void setAdNo(String adNo) {
        this.adNo = adNo;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public int getQz() {
        return qz;
    }

    public void setQz(int qz) {
        this.qz = qz;
    }

    public int getLbtime() {
        return lbtime;
    }

    public void setLbtime(int lbtime) {
        this.lbtime = lbtime;
    }

    public int getTktime() {
        return tktime;
    }

    public void setTktime(int tktime) {
        this.tktime = tktime;
    }

    public int getCycpnum() {
        return cycpnum;
    }

    public void setCycpnum(int cycpnum) {
        this.cycpnum = cycpnum;
    }

    public int getSxdisnum() {
        return sxdisnum;
    }

    public void setSxdisnum(int sxdisnum) {
        this.sxdisnum = sxdisnum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    public String getUpdateDate() {
        return updateDate;
    }



    public String getCreateDate() {
        return createDate;
    }



    public int getIsspecial() {
        return isspecial;
    }

    public void setIsspecial(int isspecial) {
        this.isspecial = isspecial;
    }

    public int getIscirclead() {
        return iscirclead;
    }

    public void setIscirclead(int iscirclead) {
        this.iscirclead = iscirclead;
    }

    public long getSingleClickNum() {
        return singleClickNum;
    }

    public void setSingleClickNum(long singleClickNum) {
        this.singleClickNum = singleClickNum;
    }

    public long getSingleExposureNum() {
        return singleExposureNum;
    }

    public void setSingleExposureNum(long singleExposureNum) {
        this.singleExposureNum = singleExposureNum;
    }

    public long getTotalClickNum() {
        return totalClickNum;
    }

    public void setTotalClickNum(long totalClickNum) {
        this.totalClickNum = totalClickNum;
    }

    public long getTotalExposureNum() {
        return totalExposureNum;
    }

    public void setTotalExposureNum(long totalExposureNum) {
        this.totalExposureNum = totalExposureNum;
    }

    public int getWifiState() {
        return wifiState;
    }

    public void setWifiState(int wifiState) {
        this.wifiState = wifiState;
    }

    public long getDayTotalClickNum() {
        return dayTotalClickNum;
    }

    public void setDayTotalClickNum(long dayTotalClickNum) {
        this.dayTotalClickNum = dayTotalClickNum;
    }

    public long getDayTotalExoisureNum() {
        return dayTotalExoisureNum;
    }

    public void setDayTotalExoisureNum(long dayTotalExoisureNum) {
        this.dayTotalExoisureNum = dayTotalExoisureNum;
    }

    public int getCyynum() {
        return cyynum;
    }

    public void setCyynum(int cyynum) {
        this.cyynum = cyynum;
    }

    public int getVideoAdSet() {
        return videoAdSet;
    }

    public void setVideoAdSet(int videoAdSet) {
        this.videoAdSet = videoAdSet;
    }

    public int getVideoAdRate() {
        return videoAdRate;
    }

    public void setVideoAdRate(int videoAdRate) {
        this.videoAdRate = videoAdRate;
    }

    public int getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(int bookLocation) {
        this.bookLocation = bookLocation;
    }

    public int getStartCha() {
        return startCha;
    }

    public void setStartCha(int startCha) {
        this.startCha = startCha;
    }

    public int getShieldAdTime() {
        return shieldAdTime;
    }

    public void setShieldAdTime(int shieldAdTime) {
        this.shieldAdTime = shieldAdTime;
    }

    public int getAdStatus() {
        return adStatus;
    }


    public int getNetState() {
        return netState;
    }

    public void setNetState(int netState) {
        this.netState = netState;
    }
}
