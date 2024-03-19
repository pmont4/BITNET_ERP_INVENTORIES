package RestAPI.Resource;

import RestAPI.Controller.EventController;
import RestAPI.Entity.Event;
import RestAPI.Util.JsonUtil;
import RestAPI.Util.MySQLDriver;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@Path("bitnet/event")
@NoArgsConstructor
public class EventResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private EventController eventController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEvent() {
        Response response;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            response = Response.ok(
                    this.getEventController().getAllEvent(connection),
                    MediaType.APPLICATION_JSON
            ).build();
        } catch(Exception e) {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity("Can't bring data due to connection errors!")
                    .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllEvent() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllEvent() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvent(@PathParam("id") Integer id) {
        Response response;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            Optional<Event> event = this.getEventController().getEvent(connection, id);
            if (event.isPresent()) {
                response = Response.ok(
                        event.get(), MediaType.APPLICATION_JSON
                ).build();
            } else {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The event with the ID: " + id + " wasn´t found int the database").build();
            }
        } catch(Exception e) {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity("Can't bring data due to connection errors!")
                    .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getEvent() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getEvent() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @POST
    @Path("add")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEvent(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            Event event = JsonUtil.getFromJson(json, new TypeReference<Event>() {
            });

            if (this.getEventController().existsEvent(connection, Integer.valueOf(String.valueOf(event.getID_EVENT())))) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The event with the ID: " + event.getID_EVENT() + " already exists in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getEventController().addEvent(connection, event), MediaType.TEXT_PLAIN
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

                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("Can't insert data due to connection errors!")
                        .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addEvent() MESSAGE: " + e);
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addEvent() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addEvent() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @DELETE
    @Path("remove/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeEvent(@PathParam("id") Integer id) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            if (!this.getEventController().existsEvent(connection, id)) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The event with the ID: " + id + " was not found in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getEventController().removeEvent(connection, id), MediaType.TEXT_PLAIN
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

                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("Can't delete data due to connection errors!")
                        .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeEvent() MESSAGE: " + e);
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeEvent() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeEvent() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEvent(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            Event event = JsonUtil.getFromJson(json, new TypeReference<Event>() {
            });

            if (Objects.isNull(event)) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The event was not found in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getEventController().updateEvent(connection, event), MediaType.TEXT_PLAIN
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

                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("Can't insert data due to connection errors!")
                        .build();

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateEvent() MESSAGE: " + e);
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateEvent() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateEvent() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    private EventController getEventController() {
        if (eventController == null) {
            eventController = new EventController();
        }

        return eventController;
    }
}
