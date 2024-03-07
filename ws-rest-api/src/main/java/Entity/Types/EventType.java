package Entity.Types;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class EventType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer ID_EVENT_TYPE;
    private String NAME;

}
