package RestAPI.Resource;

import RestAPI.Controller.RoleController;
import RestAPI.Entity.Role;
import RestAPI.Util.JsonUtil;
import RestAPI.Util.MySQLDriver;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.NoArgsConstructor;

@Path("bitnet/role")
@NoArgsConstructor
public class RoleResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private RoleController roleController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles() {
        Response response;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            response = Response.ok(
                    this.getRoleController().getAllRoles(connection),
                    MediaType.APPLICATION_JSON
            ).build();
        } catch (Exception e) {
            response = Response.status(Status.NOT_FOUND)
                    .entity("Can't bring data due to connection errors!")
                    .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllRoles() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllRoles() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @GET
    @Path("allowedmenu/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllowedMenusForRole(@PathParam("id") Integer id) {
        Response response;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            response = Response.ok(
                    this.getRoleController().getAllRoleMenu(connection, id),
                    MediaType.APPLICATION_JSON
            ).build();
        } catch (Exception e) {
            response = Response.status(Status.NOT_FOUND)
                    .entity("Can't bring data due to connection errors!")
                    .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllowedMenusForRole() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllowedMenusForRole() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRole(@PathParam("id") Integer id) {
        Response response;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            Optional<Role> role = this.getRoleController().getRole(connection, id);
            if (role.isPresent()) {
                response = Response.ok(
                        role.get(), MediaType.APPLICATION_JSON
                ).build();
            } else {
                response = Response.status(Status.NOT_FOUND)
                        .entity("The role with the ID: " + id + " wasn´t found int the database").build();
            }
        } catch (Exception e) {
            response = Response.status(Status.NOT_FOUND)
                    .entity("Can't bring data due to connection errors!")
                    .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getRole() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getRole() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @POST
    @Path("add")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRole(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            Role role = JsonUtil.getFromJson(json, new TypeReference<Role>() {
            });

            if (this.getRoleController().existsRole(connection, Integer.valueOf(String.valueOf(role.getID_ROLE())))) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The role with the ID: " + role.getID_ROLE() +  " already exists in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getRoleController().addRole(connection, role), MediaType.TEXT_PLAIN
                ).build();

                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }

                response = Response.status(Status.NOT_FOUND)
                        .entity("Can't insert data due to connection errors!")
                        .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addRole() MESSAGE: " + e);
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addRole() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addRole() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @DELETE
    @Path("remove/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeRole(@PathParam("id") Integer id) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            if (!this.getRoleController().existsRole(connection, id)) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The role with the ID: " + id + " was not found in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getRoleController().removeRole(connection, id), MediaType.TEXT_PLAIN
                ).build();

                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }

                response = Response.status(Status.NOT_FOUND)
                        .entity("Can't delete data due to connection errors!")
                        .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeRole() MESSAGE: " + e);
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeRole() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeRole() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRole(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            Role role = JsonUtil.getFromJson(json, new TypeReference<Role>() {
            });

            if (!this.getRoleController().existsRole(connection, Integer.valueOf(String.valueOf(role.getID_ROLE())))) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The role with the ID: " + role.getID_ROLE() + " was not found in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getRoleController().updateRole(connection, role), MediaType.TEXT_PLAIN
                ).build();

                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }

                response = Response.status(Status.NOT_FOUND)
                        .entity("Can't insert data due to connection errors!")
                        .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateRole() MESSAGE: " + e);
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateRole() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateRole() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    private RoleController getRoleController() {
        if (roleController == null) {
            roleController = new RoleController();
        }

        return roleController;
    }
}
