package RestAPI.Resource;

import RestAPI.Controller.EventTypeController;
import RestAPI.Entity.Types.EventType;
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
import java.util.Optional;

@Path("bitnet/eventtype")
@NoArgsConstructor
public class EventTypeResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private EventTypeController eventTypeController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEventType() {
        Response response;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            response = Response.ok(
                    this.getEventTypeController().getAllEventTypes(connection),
                    MediaType.APPLICATION_JSON
            ).build();
        } catch (Exception e) {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity("Can't bring data due to connection errors!")
                    .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllEventType() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllEventType() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventType(@PathParam("id") Integer id) {
        Response response;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            Optional<EventType> eventType = this.getEventTypeController().getEventType(connection, id);
            if (eventType.isPresent()) {
                response = Response.ok(
                        eventType.get(), MediaType.APPLICATION_JSON
                ).build();
            } else {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The event type with the ID: " + id + " wasn´t found int the database").build();
            }
        } catch (Exception e) {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity("Can't bring data due to connection errors!")
                    .build();

            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getEventType() MESSAGE: " + e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getEventType() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @POST
    @Path("add")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEventType(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            EventType eventType = JsonUtil.getFromJson(json, new TypeReference<EventType>() {
            });

            if (this.getEventTypeController().existsEventType(connection, Integer.valueOf(String.valueOf(eventType.getID_EVENT_TYPE())))) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The event type with the ID: " + eventType.getID_EVENT_TYPE() + " already exists in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getEventTypeController().addEventType(connection, eventType), MediaType.TEXT_PLAIN
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

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addEventType() MESSAGE: " + e);
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addEventType() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addEventType() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @DELETE
    @Path("remove/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeEventType(@PathParam("id") Integer id) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            if (!this.getEventTypeController().existsEventType(connection, id)) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The event type with the ID: " + id + " was not found in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getEventTypeController().removeEventType(connection, id), MediaType.TEXT_PLAIN
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

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeEventType() MESSAGE: " + e);
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeEventType() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeEventType() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEventType(String json) {
        Response response = null;

        MySQLDriver driver = new MySQLDriver();
        Connection connection = driver.getConnection();

        try {
            connection.setAutoCommit(false);

            EventType eventType = JsonUtil.getFromJson(json, new TypeReference<EventType>() {
            });

            if (!this.getEventTypeController().existsEventType(connection, Integer.valueOf(String.valueOf(eventType.getID_EVENT_TYPE())))) {
                response = Response.status(Response.Status.NOT_FOUND)
                        .entity("The event type with the ID:" + eventType.getID_EVENT_TYPE() + " was not found in the database.")
                        .build();
            } else {
                response = Response.ok(
                        this.getEventTypeController().updateEventType(connection, eventType), MediaType.TEXT_PLAIN
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

                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateEventType() MESSAGE: " + e);
            } catch (SQLException e1) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateEventType() MESSAGE-ROLLBACK: " + e1);
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateEventType() MESSAGE-FINALLY: " + e);
            }
        }

        return response;
    }

    private EventTypeController getEventTypeController() {
        if (eventTypeController == null) {
            eventTypeController = new EventTypeController();
        }

        return eventTypeController;
    }
}
