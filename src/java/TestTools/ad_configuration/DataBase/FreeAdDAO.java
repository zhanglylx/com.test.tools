package TestTools.ad_configuration.DataBase;

import TestTools.ad_configuration.AdDataBse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FreeAdDAO {
    public final String APPNAME_CXB = "cxb";
    public final String APPNAME_MFZS = "mfzs";
    public final String APPNAME_IKS = "aks";
    private final String FREE_AD_ALL = "SELECT id,adindexno,appname,adname FROM freead " +
            "WHERE appname LIKE ? ORDER BY createdate DESC";

    public List<FreeAd> getFreeAdAll(String appname) {
        List<FreeAd> freeAdList = new ArrayList<>();
        try {
            Connection connection = AdDataBse.getAdDataBse().getConnectDataBase().getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(FREE_AD_ALL);
            preparedStatement.setString(1, "%" + appname.trim() + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                freeAdList.add(
                        new FreeAd(resultSet.getInt("id")
                                , resultSet.getString("adindexno")
                                , resultSet.getString("appname")
                                , resultSet.getString("adname"))
                );
            }
            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return freeAdList;
    }
}
