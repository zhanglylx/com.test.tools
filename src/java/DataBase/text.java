package DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class text {
    public static void main(String[] args) {
        try {

            ConnectDataBase c = new ConnectDataBase("mysql");

            c.coonnect("192.168.1.246:3306/freezwsc","root_rw","loto5522");
            ResultSet rs =  c.selectSql("select * from freeadrelease");
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
            rs =  c.selectSql("select * from freeadrelease");
             job = null;
             id = null;
            while (rs.next()) {
                //获取stuname这列数据
                job = rs.getString("adNo");
                //获取stuid这列数据
                id = rs.getString("id");
                //输出结果
                System.out.println(id + "\t" + job);
            }
            c.closeDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
