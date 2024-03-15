package RestAPI.Controller;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import RestAPI.Entity.Types.MenuType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MenuTypeController  implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<MenuType> getAllMenuType(Connection connection) {
        List<MenuType> result = new ArrayList<>();

        try {
            String sql = "SELECT MT.ID_MENU_TYPE, MT.NAME FROM MENU_TYPE MT";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MenuType menuType = new MenuType();
                menuType.setID_MENU_TYPE(rs.getLong(1));
                menuType.setNAME(rs.getString(2));
                result.add(menuType);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllMenuType() MESSAGE: " + e);
        }

        return result;
    }

    public Optional<MenuType> getMenuType(Connection connection, Integer id)  {
        Optional<MenuType> opt = Optional.empty();

        try {
            String sql = "SELECT MT.ID_MENU_TYPE, MT.NAME FROM MENU_TYPE MT WHERE MT.ID_MENU_TYPE = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                MenuType menuType = new MenuType();
                menuType.setID_MENU_TYPE(rs.getLong(1));
                menuType.setNAME(rs.getString(2));
                opt = Optional.of(menuType);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getMenuType() MESSAGE: " + e);
        }

        return opt;
    }

    public String addMenuType(Connection connection, MenuType menuType) {
        String result = "";

        try {
            long id_menu_type = 0L;

            String maxId = "SELECT IFNULL(MAX(MT.ID_MENU_TYPE) + 1, 1) FROM MENU_TYPE MT";
            PreparedStatement stmt1 = connection.prepareStatement(maxId);
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                id_menu_type = rs.getLong(1);
            }
            rs.close();
            stmt1.close();

            String insert = "INSERT INTO MENU_TYPE(ID_MENU_TYPE, NAME) VALUES (?, ?)";
            PreparedStatement stmt2 = connection.prepareStatement(insert);
            stmt2.setLong(1, id_menu_type);
            stmt2.setString(2, menuType.getNAME());
            stmt2.executeUpdate();
            stmt2.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addMenuType() MESSAGE: " + e);
        }

        return result;
    }

    public String removeMenuType(Connection connection, Integer id) {
        String result = "";

        try {
            List<Long> idListMenu = new ArrayList<>();

            String sql = "SELECT M.ID_MENU FROM MENU M WHERE M.ID_MENU_TYPE = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long idMenu = rs.getLong(1);
                idListMenu.add(idMenu);
            }
            rs.close();
            stmt.close();

            if (idListMenu.size() > 1 && !idListMenu.isEmpty()) {
                idListMenu.forEach(i -> {
                    try {
                        String delete = "DELETE FROM MENU_ROLE MR WHERE MR.ID_MENU = ?";
                        PreparedStatement stmt2 = connection.prepareStatement(delete);
                        stmt2.setLong(1, i);
                        stmt2.executeUpdate();
                        stmt2.close();
                    } catch (SQLException e) {
                        System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeMenuType() MESSAGE: " + e);
                    }
                });
                idListMenu.forEach(i -> {
                    try {
                        String delete = "DELETE FROM MENU M WHERE M.ID_MENU_TYPE = ?";
                        PreparedStatement stmt2 = connection.prepareStatement(delete);
                        stmt2.setLong(1, i);
                        stmt2.executeUpdate();
                        stmt2.close();
                    } catch (SQLException e) {
                        System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeMenuType() MESSAGE: " + e);
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeMenuType() MESSAGE: " + e);
        }

        return result;
    }
}
