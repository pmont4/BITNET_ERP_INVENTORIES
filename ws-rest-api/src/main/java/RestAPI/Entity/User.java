package RestAPI.Entity;

import java.io.Serializable;

import jakarta.annotation.Nullable;
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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long ID_USER;
    private String LOGIN_NAME;
    private String PASSWORD;
    private String USERNAME;
    private String EMAIL;

    @Nullable
    private Role ROLE;
    
    private String STATUS;
 
}
