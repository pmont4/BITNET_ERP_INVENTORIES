package RestAPI.Resource;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import RestAPI.Controller.UserController;
import RestAPI.Entity.User;
import RestAPI.Util.JsonUtil;
import RestAPI.Util.MySQLDriver;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.NoArgsConstructor;

@Path("bitnet/user")
@NoArgsConstructor
public class UserResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserController userController;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            response = Response.ok(
                this.getUserController().getAllUsers(connection), 
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
    public Response getUser(@PathParam("id") Integer id) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            Optional<User> user = this.getUserController().getUser(connection, id);
            if (user.isPresent()) {
                response = Response.ok(
                    user.get(), MediaType.APPLICATION_JSON
                ).build();
            } else {
                response = Response.status(Status.NOT_FOUND)
                            .entity("The user with the ID: " + id + " wasn´t found int the database").build();
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

    @POST
    @Path("add")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            User user = JsonUtil.getFromJson(json, new TypeReference<User>() { 
            });

            response = Response.ok(
                this.getUserController().addUser(connection, user), MediaType.TEXT_PLAIN
            ).build();

            connection.commit();
            connection.setAutoCommit(true);
        } catch(SQLException | JsonProcessingException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.commit();
                    connection.setAutoCommit(true);
                }
    
                response = Response.status(Status.NOT_FOUND)
                    .entity("Can't insert data due to connection errors!")
                    .build();
    
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE: " + e.toString());
            } catch(SQLException e1) {
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

    @DELETE
    @Path("remove/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeUser(@PathParam("id") Integer id) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            response = Response.ok(
                this.getUserController().removeUser(connection, id), MediaType.TEXT_PLAIN
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
                    .entity("Can't delete data due to connection errors!")
                    .build();
    
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE: " + e.toString());
            } catch(SQLException e1) {
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

    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            User user = JsonUtil.getFromJson(json, new TypeReference<User>() { 
            });

            response = Response.ok(
                this.getUserController().updateUser(connection, user), MediaType.TEXT_PLAIN
            ).build();

            connection.commit();
            connection.setAutoCommit(true);
        } catch(SQLException | JsonProcessingException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.commit();
                    connection.setAutoCommit(true);
                }
    
                response = Response.status(Status.NOT_FOUND)
                    .entity("Can't update data due to connection errors!")
                    .build();
    
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " MESSAGE: " + e.toString());
            } catch(SQLException e1) {
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

    private UserController getUserController() {
        if (userController == null) {
            userController = new UserController();
        }

        return userController;
    }
}
