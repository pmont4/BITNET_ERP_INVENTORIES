package RestAPI.Controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import RestAPI.Entity.Menu;
import RestAPI.Entity.Types.MenuType;

public class MenuController implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Menu> getAllMenu(Connection connection) throws SQLException {
        List<Menu> result = new ArrayList<>();

        String sql = "SELECT M.ID_MENU, M.NAME, M.ID_MENU_TYPE, M.ID_PARENT_MENU FROM MENU M";

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Menu menu = new Menu();
                    menu.setID_MENU(rs.getLong(1));
                    menu.setNAME(rs.getString(2));
        
                    MenuTypeController menuTypeController = new MenuTypeController();
                    Optional<MenuType> menuType = menuTypeController
                            .getMenuType(connection, Integer.valueOf(String.valueOf(rs.getLong(3))));
                    if (menuType.isPresent()) {
                        menu.setMENU_TYPE(menuType.get());
                    }
        
                    menu.setPARENT_MENU(null);
        
                    result.add(menu);
                }
            }
        }

        return result;
    }

    public Optional<Menu> getMenu(Connection connection, Integer id) throws SQLException {
        Optional<Menu> opt = Optional.empty();

        String sql = "SELECT M.ID_MENU, M.NAME, M.ID_MENU_TYPE, M.ID_PARENT_MENU FROM MENU M WHERE M.ID_MENU = " + id;

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Menu menu = new Menu();
                    menu.setID_MENU(rs.getLong(1));
                    menu.setNAME(rs.getString(2));
        
                    MenuTypeController menuTypeController = new MenuTypeController();
                    Optional<MenuType> menuType = menuTypeController
                            .getMenuType(connection, Integer.valueOf(String.valueOf(rs.getLong(3))));
                    if (menuType.isPresent()) {
                        menu.setMENU_TYPE(menuType.get());
                    }
        
                    menu.setPARENT_MENU(null);
        
                    opt = Optional.of(menu);
                }
            }
        }

        return opt;
    }

}
