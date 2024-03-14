package RestAPI.Controller;

import RestAPI.Entity.Menu;
import RestAPI.Entity.Role;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class RoleController implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Role> getAllRoles(Connection connection) {
        List<Role> result = new ArrayList<>();

        try {
            String sql = "SELECT R.ID_ROLE, R.NAME FROM ROLE R";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Role role = new Role();
                role.setID_ROLE(rs.getLong(1));
                role.setNAME(rs.getString(2));

                List<Menu> menuList = this.getAllRoleMenu(connection, Integer.valueOf(String.valueOf(rs.getString(1))));
                role.setLST_MENU(menuList);

                result.add(role);
            }
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllRoles() MESSAGE: " + e);
        }

        return result;
    }

    public Optional<Role> getRole(Connection connection, Integer id) {
        Optional<Role> opt = Optional.empty();

        try {
            String sql = "SELECT R.ID_ROLE, R.NAME FROM ROLE R WHERE R.ID_ROLE = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setID_ROLE(rs.getLong(1));
                role.setNAME(rs.getString(2));

                List<Menu> menuList = this.getAllRoleMenu(connection, Integer.valueOf(String.valueOf(rs.getString(1))));
                role.setLST_MENU(menuList);

                opt = Optional.of(role);
            }
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getRole() MESSAGE: " + e);
        }

        return opt;
    }

    public List<Menu> getAllRoleMenu(Connection connection, Integer id) {
        List<Menu> result = new ArrayList<>();

        MenuController menuContoller = new MenuController();

        try {
            String sql = "SELECT MR.ID_ROLE, MR.ID_MENU FROM MENU_ROLE MR WHERE MR.ID_ROLE = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Optional<Menu> menu = menuContoller.getMenu(connection, Integer.valueOf(String.valueOf(rs.getLong(2))));
                menu.ifPresent(result::add);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllRoleMenu() MESSAGE: " + e);
        }

        return result;
    }

    public String addRole(Connection connection, Role role) {
        String result = "";

        try {
            long id_rol = 0L;

            String maxId = "SELECT IFNULL(MAX(R.ID_ROLE) + 1, 1) FROM ROLE R";
            PreparedStatement stmt1 = connection.prepareStatement(maxId);
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                id_rol = rs.getLong(1);
            }
            rs.close();
            stmt1.close();

            String insert = "INSERT INTO ROLE(ID_ROLE, NAME) VALUES (?, ?)";
            PreparedStatement stmt2 = connection.prepareStatement(insert);
            stmt2.setLong(1, id_rol);
            stmt2.setString(2, role.getNAME());
            stmt2.executeUpdate();
            stmt2.close();

            if (role.getLST_MENU() != null) {
                for (Menu menu : role.getLST_MENU()) {
                    String sqlMenu = "INSERT INTO MENU_ROLE (ID_ROLE, ID_MENU) VALUES (?, ?)";
                    PreparedStatement pstmt2 = connection.prepareStatement(sqlMenu);
                    pstmt2.setLong(1, id_rol);
                    pstmt2.setLong(2, menu.getID_MENU());
                    pstmt2.executeUpdate();
                    pstmt2.close();
                }
                result = "Role with the ID: " + id_rol + " correctly inserted in the table role.";
            }
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addRole() MESSAGE: " + e);
        }

        return result;
    }

    public String removeRole(Connection connection, Integer id)  {
        String result = "";

        try {
            String sql = "DELETE FROM ROLE R WHERE R.ID_ROLE = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(String.valueOf(id)));
            pstmt.executeUpdate();
            pstmt.close();

            result = "Role with the ID: " + id + " has been removed from the database and it's role menu permissions.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeRole() MESSAGE: " + e);
        }

        return result;
    }

    public String updateRole(Connection connection, Role role) {
        String result = "";

        try {
            String sql = "UPDATE ROLE R SET R.NAME = ? WHERE R.ID_ROLE = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, role.getNAME());
            stmt.setLong(2, role.getID_ROLE());
            stmt.executeUpdate();
            stmt.close();

            sql = "DELETE FROM MENU_ROLE MR WHERE MR.ID_ROLE = ?";
            PreparedStatement stmt1 = connection.prepareStatement(sql);
            stmt1.setLong(1, role.getID_ROLE());
            stmt1.executeUpdate();
            stmt1.close();

            if (role.getLST_MENU() != null) {
                for (Menu menu : role.getLST_MENU()) {
                    String sql_menu = "INSERT INTO MENU_ROLE (ID_ROLE, ID_MENU) VALUES (?, ?)";
                    PreparedStatement pstmt2 = connection.prepareStatement(sql_menu);
                    pstmt2.setLong(1, role.getID_ROLE());
                    pstmt2.setLong(2, menu.getID_MENU());
                    pstmt2.executeUpdate();
                    pstmt2.close();
                }
            }

            result = "Role with the ID: " + role.getID_ROLE() + " has been modified.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateRole() MESSAGE: " + e);
        }

        return result;
    }
}
