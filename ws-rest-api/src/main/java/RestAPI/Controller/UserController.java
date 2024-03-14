package RestAPI.Controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public List<User> getAllUsers(Connection connection) throws SQLException {
        List<User> list = new ArrayList<>();

        String sql = "SELECT U.ID_USER, U.LOGIN_NAME, REPEAT('*', LENGTH(U.PASSWORD)), U.USERNAME, U.EMAIL, U.ID_ROLE, "
                +
                "CASE " +
                "WHEN U.STATUS = 1 THEN 'ACTIVE'" +
                "ELSE 'NOT ACTIVE'" +
                "END " +
                "FROM USER U";

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                RoleController roleController = new RoleController();

                while (rs.next()) {
                    User user = new User();
                    user.setID_USER(rs.getLong(1));
                    user.setLOGIN_NAME(rs.getString(2));
                    user.setPASSWORD(rs.getString(3));
                    user.setUSERNAME(rs.getString(4));
                    user.setEMAIL(rs.getString(5));

                    Long id_role = rs.getLong(6);
                    if (Integer.valueOf(String.valueOf(id_role)) == 0) {
                        user.setROLE(null);
                    } else {
                        Optional<Role> role = roleController.getRole(connection, Integer.valueOf(String.valueOf(id_role)));
                        if (role.isPresent()) {
                            user.setROLE(role.get());
                        }
                    }

                    user.setSTATUS(rs.getString(7));
                    list.add(user);
                }

                return list;
            }
        }
    }

    public Optional<User> getUser(Connection connection, Integer id) throws SQLException {
        Optional<User> opt = Optional.empty();

        String sql = "SELECT U.ID_USER, U.LOGIN_NAME, REPEAT('*', LENGTH(U.PASSWORD)), U.USERNAME, U.EMAIL, U.ID_ROLE, "
                +
                "CASE " +
                "WHEN U.STATUS = 1 THEN 'ACTIVE'" +
                "ELSE 'NOT ACTIVE'" +
                "END " +
                "FROM USER U WHERE U.ID_USER = " + id;

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                RoleController roleController = new RoleController();

                if (rs.next()) {
                    User user = new User();
                    user.setID_USER(rs.getLong(1));
                    user.setLOGIN_NAME(rs.getString(2));
                    user.setPASSWORD(rs.getString(3));
                    user.setUSERNAME(rs.getString(4));
                    user.setEMAIL(rs.getString(5));

                    Long id_role = rs.getLong(6);
                    if (Integer.parseInt(String.valueOf(id_role)) == 0) {
                        user.setROLE(null);
                    } else {
                        Optional<Role> role = roleController.getRole(connection, Integer.valueOf(String.valueOf(id_role)));
                        role.ifPresent(user::setROLE);
                    }

                    user.setSTATUS(rs.getString(7));
                    opt = Optional.of(user);
                }
            }
        }

        return opt;
    }

    public String addUser(Connection connection, User user) throws SQLException {
        String result = null;
        
        long id_user = 0L;

        String sql = "SELECT IFNULL(MAX(U.ID_USER) + 1, 1) FROM USER U";
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    id_user = rs.getLong(1);
                }
            }
        }

        sql = "INSERT INTO USER(ID_USER, LOGIN_NAME, PASSWORD, USERNAME, EMAIL, ID_ROLE, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id_user);
            pstmt.setString(2, user.getLOGIN_NAME());
            pstmt.setString(3, user.getPASSWORD());
            pstmt.setString(4, user.getUSERNAME());
            pstmt.setString(5, user.getEMAIL());

            if (Objects.nonNull(user.getROLE())) {
                pstmt.setLong(6, user.getROLE().getID_ROLE());
            } else {
                pstmt.setLong(6, 0);
            }
            
            switch (user.getSTATUS()) {
                case "ACTIVE":
                    pstmt.setShort(7, (short) 1);
                    break;
                case "NOT ACTIVE":
                    pstmt.setShort(7, (short) 0);
                    break;
            }
            
            pstmt.executeUpdate();
            result = "User with the ID: " + id_user + " correctly inserted in the table user.";
        }

        return result;
    }

    public String removeUser(Connection connection, Integer id) throws SQLException {
        String result = null;

        String sql = "DELETE FROM USER U WHERE U.ID_USER = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, Long.parseLong(String.valueOf(id)));
            stmt.executeUpdate();

            result = "The user with the ID: " + id + " has been removed of the database.";
        }

        return result;
    }

    public String updateUser(Connection connection, User user) throws SQLException {
        String result = null;

        String sql = "UPDATE USER U SET U.LOGIN_NAME = ?, U.PASSWORD = ?, U.USERNAME = ?, U.EMAIL = ?, U.ID_ROLE = ?, U.STATUS = ? WHERE U.ID_USER = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
            result = "The user with the ID: " + user.getID_USER() + " has been modified.";
        }

        return result;
    }

}
