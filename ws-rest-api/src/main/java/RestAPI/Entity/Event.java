package RestAPI.Entity;

import java.io.Serializable;
import java.util.Date;

import RestAPI.Entity.Types.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long ID_EVENT;
    private EventType EVENT_TYPE;
    private User USER;
    private String LOG_DATE;
    private String TIME;
    private String DESCRIPTION;

}
