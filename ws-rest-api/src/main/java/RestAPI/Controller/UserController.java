package RestAPI.Controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import RestAPI.Entity.Role;
import RestAPI.Entity.User;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

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
                    "REPEAT('*', 8), " +
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
                    "REPEAT('*', 8), " +
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
            stmt2.setString(3, DigestUtils.sha256Hex(user.getPASSWORD()));
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
            result = "The user with the name: " + user.getUSERNAME() + " and the ID: " + id_user + " has been correctly created to the database.";
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
            String sql = "UPDATE USER U SET U.LOGIN_NAME = ?, U.USERNAME = ?, U.EMAIL = ?, U.ID_ROLE = ?, U.STATUS = ? WHERE U.ID_USER = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getLOGIN_NAME());
            stmt.setString(2, user.getUSERNAME());
            stmt.setString(3, user.getEMAIL());
            stmt.setLong(4, user.getROLE().getID_ROLE());

            switch (user.getSTATUS()) {
                case "ACTIVE":
                    stmt.setShort(5, (short) 1);
                    break;
                case "NOT ACTIVE":
                    stmt.setShort(5, (short) 0);
                    break;
            }

            stmt.setLong(6, user.getID_USER());

            stmt.executeUpdate();
            stmt.close();
            result = "The user with the ID: " + user.getID_USER() + " has been modified.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateUser() MESSAGE: " + e);
        }

        return result;
    }

    public HashMap<Integer, String> updatePass(Connection connection, Integer id, String newPass) {
        HashMap<Integer, String> result = new HashMap<>();

        try {
            boolean canUpdate;

            String sql = "SELECT U.HAS_TO_UPDATE_PASS FROM USER U WHERE U.ID_USER=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                short value = rs.getShort(1);
                canUpdate = value == (short) 1;
                if (!canUpdate) {
                    result.put(405, "Cannot update the password for user with the ID: " + id + " because it's not allowed to do it!");
                } else {
                    Pattern checkNums = Pattern.compile("([0-9])");
                    Pattern checkCapitalLetters = Pattern.compile("([A-Z])");

                    Matcher matchNums = checkNums.matcher(newPass);
                    Matcher matchCapitalLetters = checkCapitalLetters.matcher(newPass);

                    boolean pass_equal_to_old = false;

                    String check_equal_sql = "SELECT U.PASSWORD FROM USER U WHERE U.ID_USER=?";
                    PreparedStatement stmt_check_equal = connection.prepareStatement(check_equal_sql);
                    stmt_check_equal.setLong(1, Long.valueOf(id));
                    ResultSet rs_check = stmt_check_equal.executeQuery();
                    if (rs_check.next()) {
                        String old_pass = DigestUtils.sha256Hex(rs_check.getString(1));
                        pass_equal_to_old = DigestUtils.sha256Hex(newPass).equals(old_pass);
                    }

                    if (newPass.length() < 8) {
                        result.put(400, "Cannot update the password for user with the ID: " + id + " because it's lower than 8 characters!");
                    } else if (!matchNums.find()) {
                        result.put(400, "Cannot update the password for user with the ID: " + id + " because it does not contains any numbers!");
                    } else if (!matchCapitalLetters.find()) {
                        result.put(400, "Cannot update the password for user with the ID: " + id + " because it does not contains any capital letter!");
                    } else if (pass_equal_to_old) {
                        result.put(400, "Cannot update the password for user with the ID: " + id + " because the new password it's same one than the older one");
                    } else {
                        String sql_update = "UPDATE USER U SET U.PASSWORD=?, U.HAS_TO_UPDATE_PASS = ? WHERE U.ID_USER=?";
                        PreparedStatement stmt_update = connection.prepareStatement(sql_update);
                        stmt_update.setString(1, DigestUtils.sha256Hex(newPass));
                        stmt_update.setShort(2, (short) 0);
                        stmt_update.setLong(3, Long.valueOf(id));
                        stmt_update.executeUpdate();
                        stmt_update.close();

                        result.put(200, "The password for the user with the ID: " + id + " has been modified.");
                    }
                }
            } else {
                result.put(404, "Cannot find the user with the ID: " + id + ".");
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            result.put(500, "Some errors detected! please check console log!");
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updatePass() MESSAGE: " + e);
        }

        return result;
    }

    public HashMap<Integer, Object> authentication(Connection connection, String login_name, String password) {
        HashMap<Integer, Object> result = new HashMap<>();

        RoleController roleController = new RoleController();

        try {
            boolean needsToUpdate;
            String needUpdateSQL = "SELECT U.HAS_TO_UPDATE_PASS FROM USER U WHERE U.LOGIN_NAME=?";
            PreparedStatement stmt = connection.prepareStatement(needUpdateSQL);
            stmt.setString(1, login_name);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                result.put(404, "Cannot find the user with the login name: " + login_name);
            } else {
                needsToUpdate = rs.getShort(1) == (short) 1;
                if (needsToUpdate) {
                    result.put(405, "The user with the login name: " + login_name + " needs to update his password");
                } else {
                    String user_info = "SELECT " +
                            "U.ID_USER, " +
                            "U.LOGIN_NAME, " +
                            "REPEAT('*', 8), " +
                            "U.USERNAME, " +
                            "U.EMAIL, " +
                            "U.ID_ROLE, " +
                            "CASE " +
                            "WHEN U.STATUS = 1 THEN 'ACTIVE'" +
                            "ELSE 'NOT ACTIVE'" +
                            "END " +
                            "FROM USER U " +
                            "WHERE U.LOGIN_NAME = ? AND U.PASSWORD = ?";
                    PreparedStatement stmt2 = connection.prepareStatement(user_info);
                    stmt2.setString(1, login_name);
                    stmt2.setString(2, DigestUtils.sha256Hex(password));
                    ResultSet rs2 = stmt2.executeQuery();
                    if (rs2.next()) {
                        User user = new User();
                        user.setID_USER(rs2.getLong(1));
                        user.setLOGIN_NAME(rs2.getString(2));
                        user.setPASSWORD(rs2.getString(3));
                        user.setUSERNAME(rs2.getString(4));
                        user.setEMAIL(rs2.getString(5));

                        Long id_role = rs2.getLong(6);
                        Optional<Role> role = roleController.getRole(connection, Integer.valueOf(String.valueOf(id_role)));
                        role.ifPresent(user::setROLE);
                        user.setSTATUS(rs2.getString(7));
                        result.put(200, user);
                    } else {
                        result.put(404, "The provided username or password are not correct!");
                    }

                    rs2.close();
                    stmt2.close();
                }

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            result.put(500, "Some errors detected! please check console log!");
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: authentication() MESSAGE: " + e);
        }

        return result;
    }

    public boolean existsUser(Connection connection, Integer id) {
        return this.getUser(connection, id).isPresent();
    }

}
