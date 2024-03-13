package RestAPI.Controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import RestAPI.Entity.Role;
import RestAPI.Entity.User;

public class UserController implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<User> getAllUsers(Connection connection) throws SQLException {
        List<User> result = new ArrayList<>();

        String sql = "SELECT U.ID_USER, U.LOGIN_NAME, REPEAT('*', LENGTH(U.PASSWORD)), U.USERNAME, U.EMAIL, U.ID_ROLE, " +
                "CASE " +
                "WHEN U.STATUS = 1 THEN 'ACTIVE'" +
                "ELSE 'NOT ACTIVE'" +
                "END " +
                "FROM USER U";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        RoleController roleController = new RoleController();

        while (rs.next()) {
            User user = new User();
            user.setID_USER(rs.getLong(1));
            user.setLOGIN_NAME(rs.getString(2));
            user.setPASSWORD(rs.getString(3));
            user.setUSERNAME(rs.getString(4));
            user.setEMAIL(rs.getString(5));

            Long id_role = rs.getLong(6);
            Optional<Role> role = roleController.getRole(connection, Integer.valueOf(String.valueOf(id_role)));
            if (role.isPresent()) {
                user.setROLE(role.get());
            } else {
                user.setROLE(null);
            }

            user.setSTATUS(rs.getString(7));
            result.add(user);
        }

        rs.close();
        stmt.close();

        return result;
    }

    public Optional<User> getUser(Connection connection, Integer id) throws SQLException {
        return this.getAllUsers(connection).stream().filter(u -> u.getID_USER() == Long.parseLong(String.valueOf(id))).findFirst();
    }
    
}
