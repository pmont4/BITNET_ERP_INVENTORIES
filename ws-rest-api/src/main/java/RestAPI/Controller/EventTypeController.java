package RestAPI.Controller;

import RestAPI.Entity.Types.EventType;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class EventTypeController implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<EventType> getAllEventTypes(Connection connection) {
        List<EventType> result = new ArrayList<>();

        try {
            String sql = "SELECT ET.ID_EVENT_TYPE, ET.NAME FROM EVENT_TYPE ET";

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                EventType eventType = new EventType();
                eventType.setID_EVENT_TYPE(rs.getLong(1));
                eventType.setNAME(rs.getString(2));
                result.add(eventType);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getAllEventTypes() MESSAGE: " + e);
        }

        return result;
    }

    public Optional<EventType> getEventType(Connection connection, Integer id) {
        Optional<EventType> opt = Optional.empty();

        try {
            String sql = "SELECT ET.ID_EVENT_TYPE, ET.NAME FROM EVENT_TYPE ET WHERE ET.ID_EVENT_TYPE=?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                EventType eventType = new EventType();
                eventType.setID_EVENT_TYPE(rs.getLong(1));
                eventType.setNAME(rs.getString(2));
                opt = Optional.of(eventType);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: getEventType() MESSAGE: " + e);
        }

        return opt;
    }

    public String addEventType(Connection connection, EventType eventType) {
        String result = "";

        try {
            long id_event_type = 0L;

            String maxId = "SELECT IFNULL(MAX(ET.ID_EVENT_TYPE) + 1, 1) FROM EVENT_TYPE ET";
            PreparedStatement stmt1 = connection.prepareStatement(maxId);
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                id_event_type = rs.getLong(1);
            }
            rs.close();
            stmt1.close();

            String insert = "INSERT INTO EVENT_TYPE(ID_EVENT_TYPE, NAME) VALUES (?, ?)";
            PreparedStatement stmt2 = connection.prepareStatement(insert);
            stmt2.setLong(1, id_event_type);
            stmt2.setString(2, eventType.getNAME());
            stmt2.executeUpdate();
            stmt2.close();

            result = "The event type with the name: " + eventType.getNAME() + " and the ID: " + eventType.getID_EVENT_TYPE() + " has been correctly created in the database.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: addEventType() MESSAGE: " + e);
        }

        return result;
    }

    public String removeEventType(Connection connection, Integer id) {
        String result = "";

        try {
            String sql = "DELETE FROM EVENT_TYPE ET WHERE ET.ID_EVENT_TYPE=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, Long.valueOf(id));
            stmt.executeUpdate();
            stmt.close();

            result = "The event type with the ID: " + id + " has been removed from the database.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: removeEventType() MESSAGE: " + e);
        }

        return result;
    }

    public String updateEventType(Connection connection, EventType eventType) {
        String result = "";

        try {
            String sql = "UPDATE EVENT_TYPE ET SET ET.NAME = ? WHERE ET.ID_EVENT_TYPE=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, eventType.getNAME());
            stmt.setLong(2, eventType.getID_EVENT_TYPE());
            stmt.executeUpdate();
            stmt.close();

            result = "The event type with the ID: " + eventType.getID_EVENT_TYPE() + " has been modified.";
        } catch (Exception e) {
            System.out.println("ERROR DETECTED IN CLASS: " + this.getClass().getName() + " METHOD: updateEventType() MESSAGE: " + e);
        }

        return result;
    }

    public boolean existsEventType(Connection connection, Integer id) {
        return this.getEventType(connection, id).isPresent();
    }
}
