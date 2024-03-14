package RestAPI.Resource;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import RestAPI.Controller.MenuController;
import RestAPI.Entity.Menu;
import RestAPI.Util.JsonUtil;
import RestAPI.Util.MySQLDriver;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.NoArgsConstructor;

@Path("bitnet/menu")
@NoArgsConstructor
public class MenuResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private MenuController menuController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMenu() {
        Response response;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            response = Response.ok(
                this.getMenuController().getAllMenu(connection), 
                MediaType.APPLICATION_JSON
            ).build();
        } catch(Exception e) {
            response = Response.status(Status.NOT_FOUND)
                .entity("Can't bring data due to connection errors!")
                .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllMenu() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllMenu() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMenu(@PathParam("id") Integer id) {
        Response response;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            Optional<Menu> menu = this.getMenuController().getMenu(connection, id);
            if (menu.isPresent()) {
                response = Response.ok(
                    menu.get(), MediaType.APPLICATION_JSON
                ).build();
            } else {
                response = Response.status(Status.NOT_FOUND)
                            .entity("The menu with the ID: " + id + " wasn´t found int the database").build();
            }
        } catch(Exception e) {
            response = Response.status(Status.NOT_FOUND)
                .entity("Can't bring data due to connection errors!")
                .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getMenu() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getMenu() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @POST
    @Path("add")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMenu(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            Menu menu = JsonUtil.getFromJson(json, new TypeReference<Menu>() {
            });

            response = Response.ok(
                    this.getMenuController().addMenu(connection, menu), MediaType.TEXT_PLAIN
            ).build();

            connection.commit();
            connection.setAutoCommit(true);
        } catch(Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }

                response = Response.status(Status.NOT_FOUND)
                        .entity("Can't insert data due to connection errors!")
                        .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addMenu() MESSAGE: " + e);
            } catch(SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addMenu() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addMenu() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    private MenuController getMenuController() {
        if (menuController == null) {
            menuController = new MenuController();
        }

        return menuController;
    }
    
}
