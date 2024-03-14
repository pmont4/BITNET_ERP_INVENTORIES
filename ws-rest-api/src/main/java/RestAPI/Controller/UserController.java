package RestAPI.Controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import RestAPI.Entity.Role;
import RestAPI.Entity.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserController implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<User> getAllUsers(Connection connection) {
        List<User> list = new ArrayList<>();

        RoleController roleController = new RoleController();

        try {
            String sql = "SELECT " +
                    "U.ID_USER, " +
                    "U.LOGIN_NAME, " +
                    "REPEAT('*', LENGTH(U.PASSWORD)), " +
                    "U.USERNAME, " +
                    "U.EMAIL, " +
                    "U.ID_ROLE, " +
                    "CASE " +
                    "WHEN U.STATUS = 1 THEN 'ACTIVE'" +
                    "ELSE 'NOT ACTIVE'" +
                    "END " +
                    "FROM USER U";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setID_USER(rs.getLong(1));
                user.setLOGIN_NAME(rs.getString(2));
                user.setPASSWORD(rs.getString(3));
                user.setUSERNAME(rs.getString(4));
                user.setEMAIL(rs.getString(5));

                Long id_role = rs.getLong(6);
                Optional<Role> role = roleController.getRole(connection, Integer.valueOf(String.valueOf(id_role)));
                role.ifPresent(user::setROLE);

                user.setSTATUS(rs.getString(7));
                list.add(user);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllUsers() MESSAGE: " + e);
        }

        return list;
    }

    public Optional<User> getUser(Connection connection, Integer id) {
        Optional<User> opt = Optional.empty();

        RoleController roleController = new RoleController();

        try {
            String sql = "SELECT " +
                    "U.ID_USER, " +
                    "U.LOGIN_NAME, " +
                    "REPEAT('*', LENGTH(U.PASSWORD)), " +
                    "U.USERNAME, " +
                    "U.EMAIL, " +
                    "U.ID_ROLE, " +
                    "CASE " +
                    "WHEN U.STATUS = 1 THEN 'ACTIVE'" +
                    "ELSE 'NOT ACTIVE'" +
                    "END " +
                    "FROM USER U " +
                    "WHERE U.ID_USER = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setID_USER(rs.getLong(1));
                user.setLOGIN_NAME(rs.getString(2));
                user.setPASSWORD(rs.getString(3));
                user.setUSERNAME(rs.getString(4));
                user.setEMAIL(rs.getString(5));

                Long id_role = rs.getLong(6);
                Optional<Role> role = roleController.getRole(connection, Integer.valueOf(String.valueOf(id_role)));
                role.ifPresent(user::setROLE);
                user.setSTATUS(rs.getString(7));
                opt = Optional.of(user);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getUser() MESSAGE: " + e);
        }

        return opt;
    }

    public String addUser(Connection connection, User user) {
        String result = null;

        try {
            long id_user = 0L;

            String getId = "SELECT IFNULL(MAX(U.ID_USER) + 1, 1) FROM USER U";
            PreparedStatement stmt = connection.prepareStatement(getId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id_user = rs.getLong(1);
            }
            stmt.close();

            String insert = "INSERT INTO USER(ID_USER, LOGIN_NAME, PASSWORD, USERNAME, EMAIL, ID_ROLE, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt2 = connection.prepareStatement(insert);
            stmt2.setLong(1, id_user);
            stmt2.setString(2, user.getLOGIN_NAME());
            stmt2.setString(3, user.getPASSWORD());
            stmt2.setString(4, user.getUSERNAME());
            stmt2.setString(5, user.getEMAIL());

            if (Objects.nonNull(user.getROLE())) {
                stmt2.setLong(6, user.getROLE().getID_ROLE());
            } else {
                stmt2.setLong(6, 0);
            }

            switch (user.getSTATUS()) {
                case "ACTIVE":
                    stmt2.setShort(7, (short) 1);
                    break;
                case "NOT ACTIVE":
                    stmt2.setShort(7, (short) 0);
                    break;
            }

            stmt2.executeUpdate();
            stmt2.close();
            result = "User with the ID: " + id_user + " correctly inserted in the table user.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addUser() MESSAGE: " + e);
        }

        return result;
    }

    public String removeUser(Connection connection, Integer id) {
        String result = null;

        try {
            String sql = "DELETE FROM USER U WHERE U.ID_USER = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.parseLong(String.valueOf(id)));
            stmt.executeUpdate();
            stmt.close();

            result = "The user with the ID: " + id + " has been removed of the database.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeUser() MESSAGE: " + e);
        }

        return result;
    }

    public String updateUser(Connection connection, User user) {
        String result = null;

        try {
            String sql = "UPDATE USER U SET U.LOGIN_NAME = ?, U.PASSWORD = ?, U.USERNAME = ?, U.EMAIL = ?, U.ID_ROLE = ?, U.STATUS = ? WHERE U.ID_USER = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getLOGIN_NAME());
            stmt.setString(2, user.getPASSWORD());
            stmt.setString(3, user.getUSERNAME());
            stmt.setString(4, user.getEMAIL());
            stmt.setLong(5, user.getROLE().getID_ROLE());

            switch (user.getSTATUS()) {
                case "ACTIVE":
                    stmt.setShort(6, (short) 1);
                    break;
                case "NOT ACTIVE":
                    stmt.setShort(6, (short) 0);
                    break;
            }

            stmt.setLong(7, user.getID_USER());

            stmt.executeUpdate();
            stmt.close();
            result = "The user with the ID: " + user.getID_USER() + " has been modified.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateUser() MESSAGE: " + e);
        }

        return result;
    }

}
