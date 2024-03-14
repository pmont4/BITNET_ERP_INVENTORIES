package RestAPI.Entity;

import java.io.Serializable;
import java.util.List;

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
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ID_ROLE;
    private String NAME;

    @Nullable
    private List<Menu> LST_MENU;
    
}
