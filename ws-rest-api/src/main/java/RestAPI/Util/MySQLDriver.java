package RestAPI.Util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLDriver implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public synchronized Connection getConnection() {
        Connection result;
        
        try {
            final String HOST_MYSQL_DB = "BITNET-MYSQL";
            final String USER_MYSQL_DB = "user_bitnet_erp";
            final String PASS_MYSQL_DB = "bitnet123";

            Class.forName("com.mysql.cj.jdbc.Driver");

            result = DriverManager.getConnection("jdbc:mysql://"+HOST_MYSQL_DB+":3306/DB_BITNET_ERP", USER_MYSQL_DB, PASS_MYSQL_DB);
        } catch (Exception e) {
            result = null;
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE: " + e.toString());
        }

        return result;
    }

}
