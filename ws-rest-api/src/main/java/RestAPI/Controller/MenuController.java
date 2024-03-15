package RestAPI.Controller;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import RestAPI.Entity.Menu;
import RestAPI.Entity.Types.MenuType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MenuController implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Menu> getAllMenu(Connection connection) {
        List<Menu> result = new ArrayList<>();

        MenuTypeController menuTypeController = new MenuTypeController();

        try {
            String sql = "SELECT M.ID_MENU, M.NAME, M.ID_MENU_TYPE, M.ID_PARENT_MENU FROM MENU M";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setID_MENU(rs.getLong(1));
                menu.setNAME(rs.getString(2));

                Optional<MenuType> menuType = menuTypeController
                        .getMenuType(connection, Integer.valueOf(String.valueOf(rs.getLong(3))));
                menuType.ifPresent(menu::setMENU_TYPE);

                menu.setID_PARENT_MENU(rs.getLong(4));

                result.add(menu);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllMenu() MESSAGE: " + e);
        }

        return result;
    }

    public Optional<Menu> getMenu(Connection connection, Integer id) {
        Optional<Menu> opt = Optional.empty();

        try {
            String sql = "SELECT M.ID_MENU, M.NAME, M.ID_MENU_TYPE, M.ID_PARENT_MENU FROM MENU M WHERE M.ID_MENU = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Menu menu = new Menu();
                menu.setID_MENU(rs.getLong(1));
                menu.setNAME(rs.getString(2));

                MenuTypeController menuTypeController = new MenuTypeController();
                Optional<MenuType> menuType = menuTypeController
                        .getMenuType(connection, Integer.valueOf(String.valueOf(rs.getLong(3))));
                menuType.ifPresent(menu::setMENU_TYPE);

                menu.setID_PARENT_MENU(rs.getLong(4));

                opt = Optional.of(menu);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getMenu() MESSAGE: " + e);
        }

        return opt;
    }

    public String addMenu(Connection connection, Menu menu) {
        String result = "";

        try {
            long id_menu = 0L;

            String maxId = "SELECT IFNULL(MAX(M.ID_MENU) + 1, 1) FROM MENU M";
            PreparedStatement stmt1 = connection.prepareStatement(maxId);
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                id_menu = rs.getLong(1);
            }
            rs.close();
            stmt1.close();

            String insert = "INSERT INTO MENU(ID_MENU, NAME, ID_MENU_TYPE, ID_PARENT_MENU) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt2 = connection.prepareStatement(insert);
            stmt2.setLong(1, id_menu);
            stmt2.setString(2, menu.getNAME());
            stmt2.setLong(3, menu.getMENU_TYPE().getID_MENU_TYPE());
            stmt2.setLong(4, menu.getID_PARENT_MENU());
            stmt2.executeUpdate();
            stmt2.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addMenu() MESSAGE: " + e);
        }

        return result;
    }

}
