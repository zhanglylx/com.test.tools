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
            this.con = DriverManager.getConnection("jdbc:mysql://" + url+"?useSSL=false&autoReconnect=true", username, password);
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
        }
        return preparedStatement.executeUpdate();
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


    public static void main(String[] args) {
        //声明Connection对象
        Connection con;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://192.168.1.246:3306/freezwsc";
        //MySQL配置时的用户名
        String user = "root_rw";
        //MySQL配置时的密码
        String password = "loto5522";
        //遍历查询结果集
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();
            //要执行的SQL语句
            String sql = "select * from freeadrelease";
            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("-----------------");
            System.out.println("执行结果如下所示:");
            System.out.println("-----------------");
            System.out.println("姓名" + "\t" + "职称");
            System.out.println("-----------------");

            String job = null;
            String id = null;
            while (rs.next()) {
                //获取stuname这列数据
                job = rs.getString("adNo");
                //获取stuid这列数据
                id = rs.getString("id");
                //输出结果
                System.out.println(id + "\t" + job);
            }
            rs.close();
            con.close();
        } catch (ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            System.out.println("数据库数据成功获取！！");
        }
    }

}
