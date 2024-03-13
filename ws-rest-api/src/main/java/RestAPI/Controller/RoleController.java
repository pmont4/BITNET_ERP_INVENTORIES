package RestAPI.Controller;

import RestAPI.Entity.Menu;
import RestAPI.Entity.Role;

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

    public List<Role> getAllRoles(Connection connection) throws SQLException {
        List<Role> result = new ArrayList<>();

        String sql = "SELECT R.ID_ROLE, R.NAME FROM ROLE R";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Role role = new Role();
            role.setID_ROLE(rs.getLong(1));
            role.setNAME(rs.getString(2));

            List<Menu> menuList = this.getAllRoleMenu(connection, Integer.valueOf(String.valueOf(rs.getString(1))));
            role.setLST_MENU(menuList);

            result.add(role);
        }

        rs.close();
        stmt.close();

        return result;
    }

    public Optional<Role> getRole(Connection connection, Integer id) throws SQLException {
        return this.getAllRoles(connection).stream().filter(u -> u.getID_ROLE() == Long.parseLong(String.valueOf(id))).findFirst();
    }

    public List<Menu> getAllRoleMenu(Connection connection, Integer id) throws SQLException {
        List<Menu> result = new ArrayList<>();

        String sql = "SELECT MR.ID_ROLE, MR.ID_MENU FROM MENU_ROLE MR WHERE MR.ID_ROLE = " + id;

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        MenuController menuContoller = new MenuController();

        while (rs.next()) {
            Optional<Menu> menu = menuContoller.getMenu(connection, Integer.valueOf(String.valueOf(rs.getLong(2))));
            if (menu.isPresent()) {
                result.add(menu.get());
            }
        }

        rs.close();
        stmt.close();

        return result;
    }

}
