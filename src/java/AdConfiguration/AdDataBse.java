package AdConfiguration;

import DataBase.ConnectDataBase;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import java.sql.SQLException;

public class AdDataBse {
    private ConnectDataBase connectDataBase;
    private static AdDataBse adDataBse;

    private AdDataBse() {
        try {
            this.connectDataBase = new ConnectDataBase("mysql");
            this.connectDataBase.coonnect(AdSendConfig.DATABASE_HOST,
                    AdSendConfig.DATABASE_USERNAME, AdSendConfig.DATABASE_PASSWORD);
            System.out.println(this.connectDataBase.getCon().isClosed());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CommunicationsException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static AdDataBse getAdDataBse() {
        if (adDataBse == null) {
            adDataBse = new AdDataBse();
        } else {
            try {
                if (adDataBse.connectDataBase.getCon().isClosed()) {
                    adDataBse = new AdDataBse();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                adDataBse = new AdDataBse();
            }
        }
        return adDataBse;
    }


    public static void main(String[] args) {
        AdDataBse adDataBse = getAdDataBse();
        adDataBse.connectDataBase.closeDatabase();
    }
}
