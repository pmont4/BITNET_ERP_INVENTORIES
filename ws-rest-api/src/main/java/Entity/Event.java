package Entity;

import java.io.Serializable;
import java.util.Date;

import Entity.Types.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer ID_EVENT;
    private EventType EVENT_TYPE;
    private User USER;
    private Date LOG_DATE;

}
