package DataBase;


import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class ConnectDataBase {
    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    public ConnectDataBase(String DataBaseType) throws IllegalArgumentException, ClassNotFoundException {
        if (!"mysql".toUpperCase().equals(DataBaseType.toUpperCase())) {
            throw new IllegalArgumentException("\"暂时只支持mysql数据库\"");
        }
        try {
            //加载MySql的驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动程序类，加载驱动失败！");
            e.printStackTrace();

        }

    }

    /**
     * 连接数据库
     *
     * @param url      ip地址:端口/库
     * @param username
     * @param password
     * @return
     */
    public boolean coonnect(String url, String username, String password) throws IllegalArgumentException, SQLException,
            CommunicationsException,NullPointerException {
        if (url == null) throw new IllegalArgumentException("url为空");
        if (username == null) throw new IllegalArgumentException("username为空");
        if (password == null) throw new IllegalArgumentException("password为空");
        if (!url.matches("^.+/.+$"))
            throw new IllegalArgumentException("url格式不正确:" + url);
        try {
            this.con = DriverManager.getConnection("jdbc:mysql://" + url+"?useUnicode=true&characterEncoding=utf-8&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8&zeroDateTimeBehavior=CONVERT_TO_NULL&autoReconnect=true", username, password);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.stmt = con.createStatement();
        if (!con.isClosed()) {
            System.out.println("数据库连接成功");
            return true;
        }else {
            return false;
        }

    }

    public Connection getCon() {
        return con;
    }

    /**
     * 查询select
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public ResultSet selectSql(String sql) throws SQLException {
        return stmt.executeQuery(sql);
    }

    /**
     * 增删改
     *
     * @param sql
     * @param mapStatement key:编号  value:数据类型，值
     * @return
     * @throws SQLException
     */
    public int operationSql(String sql, Map<Integer, String> mapStatement) throws SQLException {
        if (sql == null) throw new IllegalArgumentException("sql为空");
        if (mapStatement == null || mapStatement.size() == 0)
            throw new IllegalArgumentException("mapStatement为空或无内容");
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        for(Map.Entry<Integer,String> entry : mapStatement.entrySet()){
            preparedStatement.setString(entry.getKey(),entry.getValue());
            preparedStatement.close();
        }
        try {

        }finally {

        }
        return preparedStatement.executeUpdate();
    }

    public static void main(String[] args) {
        String str1 = "abc";
        String str2 = "abc";
        System.out.println(str1.indexOf(str2));
        System.out.println(str1.compareTo(str2));
    }

    public PreparedStatement getPreparedStatement(String sql,Map<Integer, String> mapStatement) throws SQLException {
        if (sql == null) throw new IllegalArgumentException("sql为空");
        if (mapStatement == null || mapStatement.size() == 0)
            throw new IllegalArgumentException("mapStatement为空或无内容");
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        for(Map.Entry<Integer,String> entry : mapStatement.entrySet()){
            preparedStatement.setString(entry.getKey(),entry.getValue());
        }
        return preparedStatement;
    }



    public Map<String, List<String>> selectSqlMap(String sql) throws SQLException {
        ResultSetMetaData data = stmt.executeQuery(sql).getMetaData();
        Map<String, List<String>> dataMap = new LinkedHashMap<>();
        List<String> list = new ArrayList<>();
        while (rs.next()) {
            for (int i = 0; i < data.getColumnCount(); i++) {
                if (dataMap.containsKey(data.getColumnName(i))) {
                    list = dataMap.get(data.getColumnName(i));
                    list.add(rs.getString(i));
                } else {
                    list = new ArrayList<>();
                    list.add(rs.getString(i));
                    dataMap.put(data.getColumnName(i), list);
                }
            }
        }
        return dataMap;
    }

    public void closeDatabase() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


}
