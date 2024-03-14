package RestAPI.Controller;

import RestAPI.Entity.Menu;
import RestAPI.Entity.Role;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class RoleController implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Role> getAllRoles(Connection connection) throws SQLException {
        List<Role> result = new ArrayList<>();

        String sql = "SELECT R.ID_ROLE, R.NAME FROM ROLE R";

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Role role = new Role();
                    role.setID_ROLE(rs.getLong(1));
                    role.setNAME(rs.getString(2));
        
                    List<Menu> menuList = this.getAllRoleMenu(connection, Integer.valueOf(String.valueOf(rs.getString(1))));
                    role.setLST_MENU(menuList);
        
                    result.add(role);
                }
            }
        }

        return result;
    }

    public Optional<Role> getRole(Connection connection, Integer id) throws SQLException {
        Optional<Role> opt = Optional.empty();

        String sql = "SELECT R.ID_ROLE, R.NAME FROM ROLE R WHERE R.ID_ROLE = " + id;

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Role role = new Role();
                    role.setID_ROLE(rs.getLong(1));
                    role.setNAME(rs.getString(2));
        
                    List<Menu> menuList = this.getAllRoleMenu(connection, Integer.valueOf(String.valueOf(rs.getString(1))));
                    role.setLST_MENU(menuList);
        
                    opt = Optional.of(role);
                }
            }
        }

        return opt;
    }

    public List<Menu> getAllRoleMenu(Connection connection, Integer id) throws SQLException {
        List<Menu> result = new ArrayList<>();

        String sql = "SELECT MR.ID_ROLE, MR.ID_MENU FROM MENU_ROLE MR WHERE MR.ID_ROLE = " + id;

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                MenuController menuContoller = new MenuController();

                while (rs.next()) {
                    Optional<Menu> menu = menuContoller.getMenu(connection, Integer.valueOf(String.valueOf(rs.getLong(2))));
                    menu.ifPresent(result::add);
                }
            }
        }

        return result;
    }

    public String addRole(Connection connection, Role role) throws SQLException {
        String result = null;
        
        long id_rol = 0L;

        String sql = "SELECT IFNULL(MAX(R.ID_ROLE) + 1, 1) FROM ROLE R";
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    id_rol = rs.getLong(1);
                }
            }
        }

        sql = "INSERT INTO ROLE(ID_ROLE, NAME) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id_rol);
            pstmt.setString(2, role.getNAME());
            pstmt.executeUpdate();

            if (role.getLST_MENU() != null) {
                for (Menu menu : role.getLST_MENU()) {
                    String sql_menu = "INSERT INTO MENU_ROLE (ID_ROLE, ID_MENU) VALUES (?, ?)";
                    try (PreparedStatement pstmt2 = connection.prepareStatement(sql_menu)) {
                        pstmt2.setLong(1, id_rol);
                        pstmt2.setLong(2, menu.getID_MENU());
                        pstmt2.executeUpdate();
                    }
                }
            }

            result = "Role with the ID: " + id_rol + " correctly inserted in the table role.";
        }

        return result;
    }

    public String removeRole(Connection connection, Integer id) throws SQLException {
        String result;

        String sql = "DELETE FROM ROLE R WHERE R.ID_ROLE = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, Long.parseLong(String.valueOf(id)));
            pstmt.executeUpdate();

            result = "Role with the ID: " + id + " has been removed from the database and it's role menu permissions.";
        }

        return result;
    }

    public String updateRole(Connection connection, Role role) throws SQLException {
        String result;

        boolean roleUpdate;
        boolean menuRoleUpdate = false;

        String sql = "UPDATE ROLE R SET R.NAME = ? WHERE R.ID_ROLE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.getNAME());
            stmt.setLong(2, role.getID_ROLE());
            stmt.executeUpdate();

            roleUpdate = true;
        }
        sql = "DELETE FROM MENU_ROLE MR WHERE MR.ID_ROLE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, role.getID_ROLE());
            stmt.executeUpdate();

            if (role.getLST_MENU() != null) {
                for (Menu menu : role.getLST_MENU()) {
                    String sql_menu = "INSERT INTO MENU_ROLE (ID_ROLE, ID_MENU) VALUES (?, ?)";
                    try (PreparedStatement pstmt2 = connection.prepareStatement(sql_menu)) {
                        pstmt2.setLong(1, role.getID_ROLE());
                        pstmt2.setLong(2, menu.getID_MENU());
                        pstmt2.executeUpdate();
                        menuRoleUpdate = true;
                    }
                }
            }
        }

        result = roleUpdate && menuRoleUpdate ? "Role with the id: " + role.getID_ROLE() + " and it's menu role updated."
                : "Role with the id: " + role.getID_ROLE();

        return result;
    }

}
