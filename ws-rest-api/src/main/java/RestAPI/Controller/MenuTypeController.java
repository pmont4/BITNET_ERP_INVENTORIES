package RestAPI.Controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import RestAPI.Entity.Types.MenuType;

public class MenuTypeController  implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<MenuType> getAllMenuType(Connection connection) throws SQLException {
        List<MenuType> result = new ArrayList<>();

        String sql = "SELECT MT.ID_MENU_TYPE, MT.NAME FROM MENU_TYPE MT";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            MenuType menuType = new MenuType();
            menuType.setID_MENU_TYPE(rs.getLong(1));
            menuType.setNAME(rs.getString(2));
            result.add(menuType);
        }

        rs.close();
        stmt.close();

        return result;
    }

    public Optional<MenuType> getMenuType(Connection connection, Integer id) throws SQLException {
        return this.getAllMenuType(connection).stream().filter(mt -> mt.getID_MENU_TYPE() == Long.parseLong(String.valueOf(id))).findFirst();
    }
}
