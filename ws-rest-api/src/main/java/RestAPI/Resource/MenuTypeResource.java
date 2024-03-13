package RestAPI.Resource;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import RestAPI.Controller.MenuTypeController;
import RestAPI.Entity.Types.MenuType;
import RestAPI.Util.MySQLDriver;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("bitnet/menutype")
public class MenuTypeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private MenuTypeController menuTypeController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMenuType() {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            response = Response.ok(
                this.getMenuTypeController().getAllMenuType(connection), 
                MediaType.APPLICATION_JSON
            ).build();

            connection.commit();
            connection.setAutoCommit(true);
        } catch(SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.commit();
                    connection.setAutoCommit(true);
                }

                response = Response.status(Status.NOT_FOUND)
                    .entity("Can't bring data due to connection errors!")
                    .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE: " + e.toString());
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE-ROLLBACK: " + e1.toString());
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE-FINALLY: " + e.toString());
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
            connection.setAutoCommit(false);

            Optional<MenuType> menutype = this.getMenuTypeController().getMenuType(connection, id);
            if (menutype.isPresent()) {
                response = Response.ok(
                    menutype.get(), MediaType.APPLICATION_JSON
                ).build();
            } else {
                response = Response.status(Status.NOT_FOUND)
                            .entity("The menu type with the ID: " + id + " wasn´t found int the database").build();
            }

            connection.commit();
            connection.setAutoCommit(true);
        } catch(SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.commit();
                    connection.setAutoCommit(true);
                }

                response = Response.status(Status.NOT_FOUND)
                    .entity("Can't bring data due to connection errors!")
                    .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE: " + e.toString());
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE-ROLLBACK: " + e1.toString());
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE-FINALLY: " + e.toString());
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
