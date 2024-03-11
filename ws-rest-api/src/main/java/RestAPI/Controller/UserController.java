package RestAPI.Controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import RestAPI.Entity.User;

public class UserController implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Connection connection = null;

        try {
            DatabaseController databaseController = new DatabaseController();
            connection = databaseController.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT U.ID_USER, U.LOGIN_NAME, U.PASSWORD, U.USERNAME, U.EMAIL, U.ID_ROLE, U.STATUS FROM USER U";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User user = new User();
                user.setID_USER(rs.getLong(1));
                user.setLOGIN_NAME(rs.getString(2));
                user.setPASSWORD("***");
                user.setUSERNAME(rs.getString(4));
                user.setEMAIL(rs.getString(5));
                user.setROLE(null);
                user.setSTATUS(rs.getInt(7));
                result.add(user);
            }
            rs.close();
            stmt.close();

            connection.commit();
            connection.setAutoCommit(true);
            
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    connection = null;
                }

                result = null;
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE: " + e.toString());
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE-ROLLBACK BLOCk: " + e1.toString());
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE-FINALLY BLOCk: " + e.toString());
            }
        }

        return result;
    }
    
}
