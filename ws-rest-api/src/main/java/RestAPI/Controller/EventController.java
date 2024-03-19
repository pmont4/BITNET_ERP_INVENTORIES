package RestAPI.Controller;

import RestAPI.Entity.Event;
import RestAPI.Entity.Types.EventType;
import RestAPI.Entity.User;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class EventController implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Event> getAllEvent(Connection connection) {
        List<Event> result = new ArrayList<>();

        EventTypeController eventTypeController = new EventTypeController();
        UserController userController = new UserController();

        try {
            String sql = "SELECT "
                            + "E.ID_EVENT,"
                            + "E.ID_EVENT_TYPE, "
                            + "E.ID_USER, "
                            + "E.LOG_DATE, "
                            + "E.DESCRIPTION "
                            + "FROM EVENT E";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Event event = new Event();
                event.setID_EVENT(rs.getLong(1));

                Optional<EventType> eventType = eventTypeController
                        .getEventType(connection, Integer.valueOf(String.valueOf(rs.getLong(2))));
                eventType.ifPresent(event::setEVENT_TYPE);

                Optional<User> user = userController
                        .getUser(connection, Integer.valueOf(String.valueOf(rs.getLong(3))));
                user.ifPresent(event::setUSER);

                java.util.Date date = this.parseDate(rs.getString(4));
                event.setLOG_DATE(new SimpleDateFormat("dd/MM/yyyy").format(date));

                String time_string = new SimpleDateFormat("HH:mm:ss").format(date);
                event.setTIME(time_string);

                event.setDESCRIPTION(rs.getString(5));
                result.add(event);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllMenu() MESSAGE: " + e);
        }

        return result;
    }

    public Optional<Event> getEvent(Connection connection, Integer id) {
        Optional<Event> opt = Optional.empty();

        EventTypeController eventTypeController = new EventTypeController();
        UserController userController = new UserController();

        try {
            String sql = "SELECT "
                    + "E.ID_EVENT,"
                    + "E.ID_EVENT_TYPE, "
                    + "E.ID_USER, "
                    + "CAST(E.LOG_DATE AS NCHAR), "
                    + "E.DESCRIPTION "
                    + "FROM EVENT E "
                    + "WHERE E.ID_EVENT = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Event event = new Event();
                event.setID_EVENT(rs.getLong(1));

                Optional<EventType> eventType = eventTypeController
                        .getEventType(connection, Integer.valueOf(String.valueOf(rs.getLong(2))));
                eventType.ifPresent(event::setEVENT_TYPE);

                Optional<User> user = userController
                        .getUser(connection, Integer.valueOf(String.valueOf(rs.getLong(3))));
                user.ifPresent(event::setUSER);

                java.util.Date date = this.parseDate(rs.getString(4));
                event.setLOG_DATE(new SimpleDateFormat("dd/MM/yyyy").format(date));

                String time_string = new SimpleDateFormat("HH:mm:ss").format(date);
                event.setTIME(time_string);

                event.setDESCRIPTION(rs.getString(5));
                opt = Optional.of(event);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getEvent() MESSAGE: " + e);
        }

        return opt;
    }

    public String addEvent(Connection connection, Event event) {
        String result = "";

        try {
            long id_event = 0L;

            String maxId = "SELECT IFNULL(MAX(E.ID_EVENT) + 1, 1) FROM EVENT E";
            PreparedStatement stmt1 = connection.prepareStatement(maxId);
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                id_event = rs.getLong(1);
            }
            rs.close();
            stmt1.close();

            String event_insert = "INSERT INTO EVENT (ID_EVENT, ID_EVENT_TYPE, ID_USER, LOG_DATE, DESCRIPTION) VALUES (?,?,?,?,?)";
            PreparedStatement stmt2 = connection.prepareStatement(event_insert);
            stmt2.setLong(1, id_event);
            stmt2.setLong(2, event.getEVENT_TYPE().getID_EVENT_TYPE());
            stmt2.setLong(3, event.getUSER().getID_USER());

            java.util.Date date = this.parseDate(event.getLOG_DATE());
            java.sql.Date date_sql = new java.sql.Date(date.getTime());

            stmt2.setDate(4, date_sql);
            stmt2.setString(5, event.getDESCRIPTION());

            stmt2.close();

            result = "The event with the ID: " + id_event + " was correctly created in the database.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addEvent() MESSAGE: " + e);
        }

        return result;
    }

    public String removeEvent(Connection connection, Integer id) {
        String result = "";

        try {
            String sql = "DELETE FROM EVENT E WHERE E.ID_EVENT=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            stmt.executeUpdate();
            stmt.close();

            result = "The event with the ID: " + id + " has been removed from the database.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeEvent() MESSAGE: " + e);
        }

        return result;
    }

    public String updateEvent(Connection connection, Event event) {
        String result = "";

        try {
            String sql = "UPDATE SET EVENT E SET E.ID_EVENT_TYPE, E.ID_USER, E.DESCRIPTION=? WHERE E.ID_EVENT=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, event.getEVENT_TYPE().getID_EVENT_TYPE());
            stmt.setLong(2, event.getUSER().getID_USER());
            stmt.setString(3, event.getDESCRIPTION());
            stmt.setLong(4, event.getID_EVENT());
            stmt.executeUpdate();

            result = "The event with the ID: " + event.getID_EVENT() + " has been correctly modified.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateEvent() MESSAGE: " + e);
        }

        return result;
    }

    public boolean existsEvent(Connection connection, Integer id) {
        return this.getEvent(connection, id).isPresent();
    }

    java.util.Date parseDate(String date) {
        java.util.Date result = null;

        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: parseDate() MESSAGE: " + e);
        }

        return result;
    }
}
