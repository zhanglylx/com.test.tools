package TestTools.ad_configuration.DataBase;

import ZLYUtils.JavaUtils;
import ZLYUtils.WindosUtils;

import java.io.Serializable;

public class FreeadReslease implements Serializable {
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
    private String popupdesc;
    private String shieldAdStation;
    private Integer jlvideoAdRate;
    private Integer chapterEnd;
    private Integer inset;
    private Integer antimisoperation;
    private Integer upanddown;
    private Integer usergroup;
    private Integer guideBt;
    private String imgUrl;
    private Integer bottom;
    private Integer regStartTime;
    private Integer regEndTime;
    //控制用户请求次数
    private Integer singleReqNum;
    private Integer dmpGroupId;
    private Integer price;
    //    自动误点击开关，0 关闭， 1开启
    private Integer acs;
    //    误点比例，整数，例如 10 为十分之一
    private Integer acr;

    private Integer clickDmpGroupId;
    private Integer clickMaxCount;
    private Integer clickPrize;
    private String clickText;

    public FreeadReslease() {
        this.tktime = 0;
        this.cycpnum = 0;
        this.sxdisnum = 0;
        this.isdel = 0;
        this.updateDate = WindosUtils.getDate();
        this.createDate = WindosUtils.getDate();
        this.isspecial = 0;
        this.iscirclead = 0;
        this.singleClickNum = 0;
        this.singleExposureNum = 0;
        this.totalClickNum = 0;
        this.totalExposureNum = 0;
        this.dayTotalClickNum = 0;
        this.dayTotalExoisureNum = 0;
        this.cyynum = 0;
        this.videoAdSet = 0;
        this.videoAdRate = 0;
        this.bookLocation = 0;
        this.startCha = 0;
        this.shieldAdTime = 0;
        this.adStatus = 0;
        this.netState = 0;
        this.antimisoperation = 0;
        this.guideBt = 1;
        this.imgUrl = "";
        this.bottom = 0;
        this.regStartTime = 0;
        this.regEndTime = 0;
        this.chapterEnd = 0;
        this.inset = 0;
        this.jlvideoAdRate = 0;
        this.upanddown = 0;
        this.acs = 0;
        this.acr = 0;
        this.singleReqNum = 0;
        this.dmpGroupId = -1;
        this.price = 100;
        this.clickDmpGroupId = -1;
        this.clickMaxCount = 1;
        this.clickPrize = 1;
        this.clickText = "";
    }


    public Integer getClickDmpGroupId() {
        return clickDmpGroupId;
    }

    public void setClickDmpGroupId(Integer clickDmpGroupId) {
        this.clickDmpGroupId = clickDmpGroupId;
    }

    public Integer getClickMaxCount() {
        return clickMaxCount;
    }

    public void setClickMaxCount(Integer clickMaxCount) {
        this.clickMaxCount = clickMaxCount;
    }

    public Integer getClickPrize() {
        return clickPrize;
    }

    public void setClickPrize(Integer clickPrize) {
        this.clickPrize = clickPrize;
    }

    public String getClickText() {
        return clickText;
    }

    public void setClickText(String clickText) {
        this.clickText = clickText;
    }

    public Integer getDmpGroupId() {
        return dmpGroupId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setDmpGroupId(Integer dmpGroupId) {
        this.dmpGroupId = dmpGroupId;
    }

    public Integer getSingleReqNum() {
        return singleReqNum;
    }

    public void setSingleReqNum(Integer singleReqNum) {
        this.singleReqNum = singleReqNum;
    }


    public Integer getAcs() {
        return acs;
    }

    public void setAcs(Integer acs) {
        this.acs = acs;
    }

    public Integer getAcr() {
        return acr;
    }

    public void setAcr(Integer acr) {
        this.acr = acr;
    }

    public Integer getGuideBt() {
        return guideBt;
    }

    public void setGuideBt(Integer guideBt) {
        this.guideBt = guideBt;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getBottom() {
        return bottom;
    }

    public void setBottom(Integer bottom) {
        this.bottom = bottom;
    }

    public Integer getRegStartTime() {
        return regStartTime;
    }

    public void setRegStartTime(Integer regStartTime) {
        this.regStartTime = regStartTime;
    }

    public Integer getRegEndTime() {
        return regEndTime;
    }

    public void setRegEndTime(Integer regEndTime) {
        this.regEndTime = regEndTime;
    }

    public Integer getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(Integer usergroup) {
        this.usergroup = usergroup;
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

    public String getPopupdesc() {
        return popupdesc;
    }

    public void setPopupdesc(String popupdesc) {
        this.popupdesc = popupdesc;
    }

    public String getShieldAdStation() {
        return shieldAdStation;
    }

    public void setShieldAdStation(String shieldAdStation) {
        this.shieldAdStation = shieldAdStation;
    }

    public Integer getJlvideoAdRate() {
        return jlvideoAdRate;
    }

    public void setJlvideoAdRate(Integer jlvideoAdRate) {
        this.jlvideoAdRate = jlvideoAdRate;
    }

    public Integer getChapterEnd() {
        return chapterEnd;
    }

    public void setChapterEnd(Integer chapterEnd) {
        this.chapterEnd = chapterEnd;
    }

    public Integer getInset() {
        return inset;
    }

    public void setInset(Integer inset) {
        this.inset = inset;
    }

    public Integer getAntimisoperation() {
        return antimisoperation;
    }

    public void setAntimisoperation(Integer antimisoperation) {
        this.antimisoperation = antimisoperation;
    }

    public Integer getUpanddown() {
        return upanddown;
    }

    public void setUpanddown(Integer upanddown) {
        this.upanddown = upanddown;
    }
}
