package RestAPI.Entity.Types;

import java.io.Serializable;

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
public class EventType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ID_EVENT_TYPE;
    private String NAME;

}
