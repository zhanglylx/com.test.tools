package TestTools.ad_configuration.DataBase;

import TestTools.ad_configuration.AdDataBse;
import ZLYUtils.WindosUtils;

import java.sql.*;

public class FreeadResleaseDAO {
    private static final String INSERT_AD = "INSERT INTO freeadrelease() values(" +
            "?,?,?,?,?," +
            "?,?,?,?,?," +
            "?,?,?,?,?," +
            "?,?,?,?,?," +
            "?,?,?,?,?," +
            "?,?,?,?,?," +
            "?,?,?,?,?," +
            "?,?,?,?,?," +
            "?,?,?,?,?," +
            "?,?,?，?,?)";
    private static final String AD_MAX = "SELECT MAX(id) as i FROM freeadrelease";

    public FreeadResleaseDAO() {
    }

    public boolean insertAD(FreeadReslease freeadReslease) throws SQLException {
        Connection connection = AdDataBse.getAdDataBse().getConnectDataBase().getCon();
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AD);
        preparedStatement.setInt(1, selectMaxId() + 1);
        preparedStatement.setString(2, freeadReslease.getAppname());
        preparedStatement.setString(3, freeadReslease.getAdvSingNo());
        preparedStatement.setString(4, freeadReslease.getAdNo());
        preparedStatement.setString(5, freeadReslease.getChannelid());
        preparedStatement.setString(6, freeadReslease.getVersion());
        preparedStatement.setString(7, freeadReslease.getRelStartDate());
        preparedStatement.setString(8, freeadReslease.getRelEndDate());
        preparedStatement.setInt(9, freeadReslease.getQz());
        preparedStatement.setInt(10, freeadReslease.getLbtime());
        preparedStatement.setInt(11, freeadReslease.getTktime());
        preparedStatement.setInt(12, freeadReslease.getCycpnum());
        preparedStatement.setInt(13, freeadReslease.getSxdisnum());
        preparedStatement.setInt(14, freeadReslease.getStatus());
        preparedStatement.setInt(15, freeadReslease.getIsdel());
        preparedStatement.setString(16, freeadReslease.getUpdateDate());
        preparedStatement.setString(17, freeadReslease.getCreateDate());
        preparedStatement.setInt(18, freeadReslease.getIsspecial());
        preparedStatement.setInt(19, freeadReslease.getIscirclead());
        preparedStatement.setLong(20, freeadReslease.getSingleClickNum());
        preparedStatement.setLong(21, freeadReslease.getSingleExposureNum());
        preparedStatement.setLong(22, freeadReslease.getTotalClickNum());
        preparedStatement.setLong(23, freeadReslease.getTotalExposureNum());
        preparedStatement.setInt(24, freeadReslease.getWifiState());
        preparedStatement.setLong(25, freeadReslease.getDayTotalClickNum());
        preparedStatement.setLong(26, freeadReslease.getDayTotalExoisureNum());
        preparedStatement.setInt(27, freeadReslease.getCyynum());
        preparedStatement.setInt(28, freeadReslease.getVideoAdSet());
        preparedStatement.setInt(29, freeadReslease.getVideoAdRate());
        preparedStatement.setInt(30, freeadReslease.getBookLocation());
        preparedStatement.setInt(31, freeadReslease.getStartCha());
        preparedStatement.setInt(32, freeadReslease.getShieldAdTime());
        preparedStatement.setInt(33, freeadReslease.getAdStatus());
        preparedStatement.setInt(34, freeadReslease.getNetState());
//        35是描述:description字段
        preparedStatement.setString(35, "松鼠工具创建:" + WindosUtils.getDate());
        preparedStatement.setString(36, freeadReslease.getPopupdesc());
        preparedStatement.setString(37, freeadReslease.getShieldAdStation());
        setIntVluesOrNull(preparedStatement, 38, freeadReslease.getJlvideoAdRate());
        setIntVluesOrNull(preparedStatement, 39, freeadReslease.getChapterEnd());
        setIntVluesOrNull(preparedStatement, 40, freeadReslease.getInset());
        setIntVluesOrNull(preparedStatement, 41, freeadReslease.getAntimisoperation());
        setIntVluesOrNull(preparedStatement, 42, freeadReslease.getUpanddown());
        setIntVluesOrNull(preparedStatement, 43, freeadReslease.getUsergroup());
        setIntVluesOrNull(preparedStatement, 44, freeadReslease.getGuideBt());
        preparedStatement.setString(45, freeadReslease.getImgUrl());
        setIntVluesOrNull(preparedStatement, 46, freeadReslease.getBottom());
        setIntVluesOrNull(preparedStatement, 47, freeadReslease.getRegStartTime());
        setIntVluesOrNull(preparedStatement, 48, freeadReslease.getRegEndTime());
        setIntVluesOrNull(preparedStatement, 49, freeadReslease.getAcs());
        setIntVluesOrNull(preparedStatement, 50, freeadReslease.getAcr());
        System.out.println(preparedStatement.toString());
        boolean b = false;
        if (preparedStatement.executeUpdate() == 1) b = true;
        preparedStatement.close();
        return b;
    }

    private void setIntVluesOrNull(PreparedStatement preparedStatement, int number, Integer values) throws SQLException {
        if (values != null) {
            preparedStatement.setInt(number, values);
        } else {
            preparedStatement.setNull(number, Types.INTEGER);
        }
    }

    public int selectMaxId() throws SQLException {
        Connection connection = AdDataBse.getAdDataBse().getConnectDataBase().getCon();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(AD_MAX);
        int i;
        if (resultSet.next()) {
            i = resultSet.getInt(1);
        } else {
            throw new IllegalArgumentException("获取freeadrelease表中的最大ID错误");
        }
        resultSet.close();
        statement.close();
        return i;
    }
}
