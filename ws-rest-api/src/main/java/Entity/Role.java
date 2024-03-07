package Entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer ID_ROLE;
    private String NAME;
    
}
