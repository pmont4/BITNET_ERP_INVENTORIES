package RestAPI.Resource;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import RestAPI.Controller.MenuTypeController;
import RestAPI.Entity.Role;
import RestAPI.Entity.Types.MenuType;
import RestAPI.Util.JsonUtil;
import RestAPI.Util.MySQLDriver;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.NoArgsConstructor;

@Path("bitnet/menutype")
@NoArgsConstructor
public class MenuTypeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private MenuTypeController menuTypeController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMenuType() {
        Response response;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            response = Response.ok(
                this.getMenuTypeController().getAllMenuType(connection), 
                MediaType.APPLICATION_JSON
            ).build();
        } catch(Exception e) {
            response = Response.status(Status.NOT_FOUND)
                .entity("Can't bring data due to connection errors!")
                .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllMenuType() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllMenuType() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMenuType(@PathParam("id") Integer id) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            Optional<MenuType> menutype = this.getMenuTypeController().getMenuType(connection, id);
            if (menutype.isPresent()) {
                response = Response.ok(
                    menutype.get(), MediaType.APPLICATION_JSON
                ).build();
            } else {
                response = Response.status(Status.NOT_FOUND)
                            .entity("The menu type with the ID: " + id + " wasn´t found int the database").build();
            }
        } catch(Exception e) {
            response = Response.status(Status.NOT_FOUND)
                .entity("Can't bring data due to connection errors!")
                .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getMenuType() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getMenuType() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @POST
    @Path("add")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMenuType(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            MenuType menuType = JsonUtil.getFromJson(json, new TypeReference<MenuType>() {
            });

            if (this.getMenuTypeController().existsMenuType(connection, Integer.valueOf(String.valueOf(menuType.getID_MENU_TYPE())))) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The menu with the ID: " + menuType.getID_MENU_TYPE() + " already exists in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getMenuTypeController().addMenuType(connection, menuType), MediaType.TEXT_PLAIN
                ).build();

                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch(Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }

                response = Response.status(Status.NOT_FOUND)
                        .entity("Can't insert data due to connection errors!")
                        .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addMenuType() MESSAGE: " + e);
            } catch(SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addMenuType() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addMenuType() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @DELETE
    @Path("remove/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeMenuType(@PathParam("id") Integer id) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            if (!this.getMenuTypeController().existsMenuType(connection, id)) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The menu with the ID: " + id + " was not found in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getMenuTypeController().removeMenuType(connection, id), MediaType.TEXT_PLAIN
                ).build();

                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch(Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }

                response = Response.status(Status.NOT_FOUND)
                        .entity("Can't delete data due to connection errors!")
                        .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeMenuType() MESSAGE: " + e);
            } catch(SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeMenuType() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeMenuType() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMediaType(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            MenuType menuType = JsonUtil.getFromJson(json, new TypeReference<MenuType>() {
            });

            if (!this.getMenuTypeController().existsMenuType(connection, Integer.valueOf(String.valueOf(menuType.getID_MENU_TYPE())))) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The menu with the ID: " + menuType.getID_MENU_TYPE() + " was not found in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getMenuTypeController().updateMenuType(connection, menuType), MediaType.TEXT_PLAIN
                ).build();

                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch(Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }

                response = Response.status(Status.NOT_FOUND)
                        .entity("Can't insert data due to connection errors!")
                        .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateMediaType() MESSAGE: " + e);
            } catch(SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateMediaType() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateMediaType() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    private MenuTypeController getMenuTypeController() {
        if (menuTypeController == null) {
            menuTypeController = new MenuTypeController();
        }

        return menuTypeController;
    }
}
