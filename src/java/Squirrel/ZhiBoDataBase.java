package Squirrel;

import DataBase.ConnectDataBase;
import SquirrelFrame.OutputText;
import ZLYUtils.SaveCrash;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import java.sql.SQLException;

public class ZhiBoDataBase {
    private ConnectDataBase cdb ;
    private OutputText opt;
    public static final String DATABASE_USER="root_rw";
    public static final String DATABASE_PASSWORD = "loto5522";
    private  boolean dataBaseOnline ;//判断数据库是否连接成功
    private static ZhiBoDataBase zhiBoDataBase;
    private ZhiBoDataBase(OutputText opt){
        dataBaseOnline=false;
        this.opt = opt;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String[] s = new String[]{".","..","..."};
                int index=0;
                while (true){
                    if(index>=s.length)index=0;
                    opt.setText("正在连接数据库，请等待"+s[index]);
                    index++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });
        t.start();
        try {
                cdb = new ConnectDataBase("mysql");
                cdb.coonnect("192.168.1.246:3306/wwlive", DATABASE_USER, DATABASE_PASSWORD);
            t.interrupt();
            if(!cdb.getCon().isClosed()){
                opt.addText("数据连接成功:192.168.1.246:3306/wwlive\n");
            }else{
                opt.setText("数据库连接失败:192.168.1.246:3306/wwlive");
            }
            dataBaseOnline=true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            opt.setText("数据库连接失败:192.168.1.246:3306/wwlive");
            cdb=null;
        } catch (CommunicationsException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            opt.setText("数据库连接失败:192.168.1.246:3306/wwlive");
            cdb=null;
        } catch (SQLException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            opt.setText("数据库连接失败:192.168.1.246:3306/wwlive");
            cdb=null;
        }finally {
            t.interrupt();
        }

    }
    public static ZhiBoDataBase getZhiBoDataBase(OutputText opt){
        if(zhiBoDataBase==null){
            zhiBoDataBase=new ZhiBoDataBase(opt);
        }else{
            try {
                if(!zhiBoDataBase.getCdb().getCon().isClosed()){
                    opt.addText("数据连接成功:192.168.1.246:3306/wwlive\n");
                }else{
                    opt.setText("数据库连接失败:192.168.1.246:3306/wwlive");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                opt.setText("数据库连接失败:192.168.1.246:3306/wwlive");
            }
        }
        return zhiBoDataBase;
    }

    public ConnectDataBase getCdb(){
        return cdb;
    }
    public boolean getdataBaseOnline(){return dataBaseOnline;}
}
