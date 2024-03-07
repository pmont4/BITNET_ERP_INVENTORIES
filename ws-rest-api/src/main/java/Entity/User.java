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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer ID_USER;
    private String LOGIN_NAME;
    private String PASSWORD;
    private String USERNAME;
    private String EMAIL;
    private Role ROLE;
    private Integer STATUS;

}
