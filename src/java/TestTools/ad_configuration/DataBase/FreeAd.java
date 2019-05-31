package TestTools.ad_configuration.DataBase;


public class FreeAd {
    private int id;
    private String adindexno;
    private String appname;
    private String adname;

    public FreeAd() {
    }

    public FreeAd(int id, String adindexno, String appname, String adname) {
        this.id = id;
        this.adindexno = adindexno;
        this.appname = appname;
        this.adname = adname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdindexno() {
        return adindexno;
    }

    public void setAdindexno(String adindexno) {
        this.adindexno = adindexno;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAdname() {
        return adname;
    }

    public void setAdname(String adname) {
        this.adname = adname;
    }
}
