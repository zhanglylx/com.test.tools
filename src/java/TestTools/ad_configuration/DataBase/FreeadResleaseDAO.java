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
            "?,?,?,?,?," +
            "?,?,?,?,?," +
            "?,?)";
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
        preparedStatement.setInt(22, freeadReslease.getSingleReqNum());
        preparedStatement.setLong(23, freeadReslease.getTotalClickNum());
        preparedStatement.setLong(24, freeadReslease.getTotalExposureNum());
        preparedStatement.setInt(25, freeadReslease.getWifiState());
        preparedStatement.setLong(26, freeadReslease.getDayTotalClickNum());
        preparedStatement.setLong(27, freeadReslease.getDayTotalExoisureNum());
        preparedStatement.setInt(28, freeadReslease.getCyynum());
        preparedStatement.setInt(29, freeadReslease.getVideoAdSet());
        preparedStatement.setInt(30, freeadReslease.getVideoAdRate());
        preparedStatement.setInt(31, freeadReslease.getBookLocation());
        preparedStatement.setInt(32, freeadReslease.getStartCha());
        preparedStatement.setInt(33, freeadReslease.getShieldAdTime());
        preparedStatement.setInt(34, freeadReslease.getAdStatus());
        preparedStatement.setInt(35, freeadReslease.getNetState());
//        35是描述:description字段
        preparedStatement.setString(36, "松鼠工具创建:" + WindosUtils.getDate());
        preparedStatement.setString(37, freeadReslease.getPopupdesc());
        preparedStatement.setString(38, freeadReslease.getShieldAdStation());
        setIntVluesOrNull(preparedStatement, 39, freeadReslease.getJlvideoAdRate());
        setIntVluesOrNull(preparedStatement, 40, freeadReslease.getChapterEnd());
        setIntVluesOrNull(preparedStatement, 41, freeadReslease.getInset());
        setIntVluesOrNull(preparedStatement, 42, freeadReslease.getAntimisoperation());
        setIntVluesOrNull(preparedStatement, 43, freeadReslease.getUpanddown());
        setIntVluesOrNull(preparedStatement, 44, freeadReslease.getUsergroup());
        setIntVluesOrNull(preparedStatement, 45, freeadReslease.getDmpGroupId());
        setIntVluesOrNull(preparedStatement, 46, freeadReslease.getGuideBt());
        preparedStatement.setString(47, freeadReslease.getImgUrl());
        setIntVluesOrNull(preparedStatement, 48, freeadReslease.getBottom());
        setIntVluesOrNull(preparedStatement, 49, freeadReslease.getRegStartTime());
        setIntVluesOrNull(preparedStatement, 50, freeadReslease.getRegEndTime());
        setIntVluesOrNull(preparedStatement, 51, freeadReslease.getAcs());
        setIntVluesOrNull(preparedStatement, 52, freeadReslease.getAcr());
        setIntVluesOrNull(preparedStatement, 53, freeadReslease.getPrice());
        setIntVluesOrNull(preparedStatement, 54, freeadReslease.getClickDmpGroupId());
        setIntVluesOrNull(preparedStatement, 55, freeadReslease.getClickMaxCount());
        setIntVluesOrNull(preparedStatement, 56, freeadReslease.getClickPrize());
        preparedStatement.setString(57, freeadReslease.getClickText());
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
