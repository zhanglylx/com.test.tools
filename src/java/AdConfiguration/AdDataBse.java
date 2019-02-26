package AdConfiguration;

import DataBase.ConnectDataBase;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdDataBse {
    public ConnectDataBase getConnectDataBase() {
        return connectDataBase;
    }

    private ConnectDataBase connectDataBase;
    private static AdDataBse adDataBse;

    private AdDataBse() {
        try {
            this.connectDataBase = new ConnectDataBase("mysql");
            System.out.println(Arrays.toString(AdSendConfig.getDataBase()));
            this.connectDataBase.coonnect(AdSendConfig.getDataBase()[0],
                    AdSendConfig.getDataBase()[1], AdSendConfig.getDataBase()[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否关闭
     *
     * @return
     */
    public boolean isClosed() {
        if (this.connectDataBase.getCon() == null) return true;
        try {
            return this.connectDataBase.getCon().isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void setAdDataBseNull() {
        try {
            adDataBse.connectDataBase.closeDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            adDataBse = null;
        }

    }

    public static AdDataBse getAdDataBse() {
        if (adDataBse == null) {
            adDataBse = new AdDataBse();
        }
        return adDataBse;
    }

    /**
     * 更新状态为下架
     *
     * @return
     */
    public int updateShelves(String appname, String version, String channelid,
                             String relStartDate, String relEndDate, int qz, int iscirclead,
                             int lbtime, int dayTotalClickNum, int dayTotalExposureNum,
                             int totalClickNum, int totalExposureNum, int singleClickNum,
                             int singleExposureNum, Long adNo, List<Integer> ads, int wifi,
                             int channelidShelves,
                             int versionShelves, int timeShelves, int lbtimeShelves, int wifiShelves,
                             int bgdjShelves) {
        String sql = "update " + AdSendConfig.DATABASE_AD_TABLE_NAME +
                " set " + AdSendConfig.STATUS + "=?" +
                " where ";
        sql = sqlEqualAdd(sql, AdSendConfig.APPNAME);
        if (versionShelves == 1) {
            sql = sqlEqualAdd(sql, AdSendConfig.VERSION);
        } else {
            sql = sqlLikeAdd(sql, AdSendConfig.VERSION);
        }
        if (channelidShelves == 1) {
            sql = sqlEqualAdd(sql, AdSendConfig.CHANNELID);
        } else {
            sql = sqlLikeAdd(sql, AdSendConfig.CHANNELID);


        }
        if (timeShelves == 1) {
            sql = sqlEqualAdd(sql, AdSendConfig.RES_START_DATE);
            sql = sqlEqualAdd(sql, AdSendConfig.REL_END_DATE);
            sql = sqlEqualAdd(sql, AdSendConfig.IS_CIRCLEAD);
        } else {
            sql = sqlLikeAdd(sql, AdSendConfig.RES_START_DATE);
            sql = sqlLikeAdd(sql, AdSendConfig.REL_END_DATE);
            sql = sqlLikeAdd(sql, AdSendConfig.IS_CIRCLEAD);

        }
        if (lbtimeShelves == 1) {
            sql = sqlEqualAdd(sql, AdSendConfig.LB_TIME);
        } else {
            sql = sqlLikeAdd(sql, AdSendConfig.LB_TIME);
        }
        if (bgdjShelves == 1) {
            sql = sqlEqualAdd(sql, AdSendConfig.DAY_TOTAL_CLICK_NUM);
            sql = sqlEqualAdd(sql, AdSendConfig.DAY_TOTAL_EXPOSURE_NUM);
            sql = sqlEqualAdd(sql, AdSendConfig.TOTAL_CLICK_NUM);
            sql = sqlEqualAdd(sql, AdSendConfig.TOTAL_EXPOSURE_NUM);
            sql = sqlEqualAdd(sql, AdSendConfig.SINGLE_CLICK_NUM);
            sql = sqlEqualAdd(sql, AdSendConfig.SINGLE_EXPOSURE_NUM);
        } else {
            sql = sqlLikeAdd(sql, AdSendConfig.DAY_TOTAL_CLICK_NUM);
            sql = sqlLikeAdd(sql, AdSendConfig.DAY_TOTAL_EXPOSURE_NUM);
            sql = sqlLikeAdd(sql, AdSendConfig.TOTAL_CLICK_NUM);
            sql = sqlLikeAdd(sql, AdSendConfig.TOTAL_EXPOSURE_NUM);
            sql = sqlLikeAdd(sql, AdSendConfig.SINGLE_CLICK_NUM);
            sql = sqlLikeAdd(sql, AdSendConfig.SINGLE_EXPOSURE_NUM);
        }
        if (adNo == 0) {
            sql = sqlLikeAdd(sql, AdSendConfig.AD_NO);
        } else {
            sql = sqlEqualAdd(sql, AdSendConfig.AD_NO);
        }

        if (wifiShelves == 1) {
            sql = sqlEqualAdd(sql, AdSendConfig.WIFI_STATE);
        } else {
            sql = sqlLikeAdd(sql, AdSendConfig.WIFI_STATE);
        }
        if (ads.size() == 0) {
            sql = sqlLikeAdd(sql, AdSendConfig.ADV_SING_NO);
        } else {
            sql = sqlEqualAdd(sql, AdSendConfig.ADV_SING_NO);
        }

        int i = 0;
        try {
            PreparedStatement preparedStatement = this.connectDataBase.getCon().prepareStatement(sql);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, appname);
            if (versionShelves == 1) {
                preparedStatement.setString(3, "%" + version + "%");
            } else {
                preparedStatement.setString(3, version);
            }
            if (channelidShelves == 1) {
                preparedStatement.setString(4, "%" + channelid + "%");
            } else {
                preparedStatement.setString(4, channelid);
            }
            if (timeShelves == 1) {
                preparedStatement.setString(5, relStartDate);
                preparedStatement.setString(6, relEndDate);
                preparedStatement.setInt(7, iscirclead);
            } else {
                preparedStatement.setString(5, "%");
                preparedStatement.setString(6, "%");
                preparedStatement.setString(7, "%");
            }
            if (lbtimeShelves == 1) {
                preparedStatement.setInt(8, lbtime);
            } else {
                preparedStatement.setString(8, "%");
            }
            if (bgdjShelves == 1) {
                preparedStatement.setInt(9, dayTotalClickNum);
                preparedStatement.setInt(10, dayTotalExposureNum);
                preparedStatement.setInt(11, totalClickNum);
                preparedStatement.setInt(12, totalExposureNum);
                preparedStatement.setInt(13, singleClickNum);
                preparedStatement.setInt(14, singleExposureNum);
            } else {
                preparedStatement.setString(9, "%");
                preparedStatement.setString(10, "%");
                preparedStatement.setString(11, "%");
                preparedStatement.setString(12, "%");
                preparedStatement.setString(13, "%");
                preparedStatement.setString(14, "%");
            }
            if (adNo == 0) {
                preparedStatement.setString(15, "%");
            } else {
                preparedStatement.setString(15, String.valueOf(adNo));
            }

            if (wifiShelves == 1) {
                preparedStatement.setInt(16, wifi);
            } else {
                preparedStatement.setString(16, "%");
            }
            if (ads.size() == 0) {
                preparedStatement.setString(17, "%");
                System.out.println(preparedStatement.toString());
                i += preparedStatement.executeUpdate();
            } else {
                for (int ad : ads) {
                    preparedStatement.setString(17, "GG-" + ad);
                    System.out.println(preparedStatement.toString());
                    i += preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setAdDataBseNull();
        }
        return i;

    }

    ;

    /**
     * 更新状态为上架
     *
     * @return
     */
    public int updateStatusOnTheShelf(String appname, String version, String channelid,
                                      String relStartDate, String relEndDate, int qz, int iscirclead,
                                      int lbtime, int dayTotalClickNum, int dayTotalExposureNum,
                                      int totalClickNum, int totalExposureNum, int singleClickNum,
                                      int singleExposureNum, Long adNo, List<Integer> ads, int wifi
    ) {

        String sql = "update " + AdSendConfig.DATABASE_AD_TABLE_NAME +
                " set " + AdSendConfig.STATUS + "=?" +
                " where ";
        sql = sqlEqualAdd(sql, AdSendConfig.APPNAME);
        sql = sqlEqualAdd(sql, AdSendConfig.VERSION);
        sql = sqlEqualAdd(sql, AdSendConfig.CHANNELID);
        sql = sqlEqualAdd(sql, AdSendConfig.RES_START_DATE);
        sql = sqlEqualAdd(sql, AdSendConfig.REL_END_DATE);
        sql = sqlEqualAdd(sql, AdSendConfig.QZ);
        sql = sqlEqualAdd(sql, AdSendConfig.IS_CIRCLEAD);
        sql = sqlEqualAdd(sql, AdSendConfig.LB_TIME);
        sql = sqlEqualAdd(sql, AdSendConfig.DAY_TOTAL_CLICK_NUM);
        sql = sqlEqualAdd(sql, AdSendConfig.DAY_TOTAL_EXPOSURE_NUM);
        sql = sqlEqualAdd(sql, AdSendConfig.TOTAL_CLICK_NUM);
        sql = sqlEqualAdd(sql, AdSendConfig.TOTAL_EXPOSURE_NUM);
        sql = sqlEqualAdd(sql, AdSendConfig.SINGLE_CLICK_NUM);
        sql = sqlEqualAdd(sql, AdSendConfig.SINGLE_EXPOSURE_NUM);
        sql = sqlEqualAdd(sql, AdSendConfig.AD_NO);
        sql = sqlEqualAdd(sql, AdSendConfig.WIFI_STATE);
        sql = sqlEqualAdd(sql, AdSendConfig.ADV_SING_NO);
        int i = 0;
        try {
            PreparedStatement preparedStatement = this.connectDataBase.getCon().prepareStatement(sql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, appname);
            preparedStatement.setString(3, version);
            preparedStatement.setString(4, channelid);
            preparedStatement.setString(5, relStartDate);
            preparedStatement.setString(6, relEndDate);
            preparedStatement.setInt(7, qz);
            preparedStatement.setInt(8, iscirclead);
            preparedStatement.setInt(9, lbtime);
            preparedStatement.setInt(10, dayTotalClickNum);
            preparedStatement.setInt(11, dayTotalExposureNum);
            preparedStatement.setInt(12, totalClickNum);
            preparedStatement.setInt(13, totalExposureNum);
            preparedStatement.setInt(14, singleClickNum);
            preparedStatement.setInt(15, singleExposureNum);
            preparedStatement.setString(16, String.valueOf(adNo));
            preparedStatement.setInt(17, wifi);
            for (int ad : ads) {
                preparedStatement.setString(18, "GG-" + ad);
                if (preparedStatement.executeUpdate() > 0) i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            setAdDataBseNull();
        }
        return i;
    }

    /**
     * 拼接SQL
     *
     * @param sql
     * @return
     */
    private String sqlEqualAdd(String sql, String key) {
        if (sql.endsWith("?")) {
            sql += " and " + key + "=?";
        } else {
            sql += key + "=?";
        }
        return sql;
    }

    private String sqlLikeAdd(String sql, String key) {
        if (sql.endsWith("?")) {
            sql += " and " + key + " like ?";
        } else {
            sql += key + " like ?";
        }
        return sql;
    }

    public void close() {
        this.connectDataBase.closeDatabase();
    }


    public static void main(String[] args) {
        AdDataBse adDataBse = getAdDataBse();
        adDataBse.connectDataBase.closeDatabase();
    }
}
