package RestAPI.Controller;

import RestAPI.Entity.Role;
import RestAPI.Util.MySQLDriver;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleController implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Role> getAllRoles() {
        List<Role> result = new ArrayList<>();
        Connection connection = null;

        try {
            MySQLDriver mySQLDriver = new MySQLDriver();
            connection = mySQLDriver.getConnection();
            connection.setAutoCommit(false);

            String sql = "SELECT R.ID_ROLE, R.NAME FROM ROLE R";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Role role = new Role();
                role.setID_ROLE(rs.getLong(1));
                role.setNAME(rs.getString(2));
                result.add(role);
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

    public Optional<Role> getRole(Integer id) {
        return this.getAllRoles().stream().filter(u -> u.getID_ROLE() == Long.parseLong(String.valueOf(id))).findFirst();
    }

}
