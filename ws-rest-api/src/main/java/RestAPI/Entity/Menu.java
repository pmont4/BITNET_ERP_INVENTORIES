package RestAPI.Entity;

import java.io.Serializable;

import RestAPI.Entity.Types.MenuType;
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
public class Menu implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long ID_MENU;
    private String NAME;
    private MenuType MENU_TYPE;

    @Nullable
    private Long ID_PARENT_MENU;

}
