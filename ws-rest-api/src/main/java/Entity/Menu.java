package Entity;

import java.io.Serializable;

import Entity.Types.MenuType;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Menu implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer ID_MENU;
    private String NAME;
    private MenuType MENU_TYPE;

    @Nullable
    private Menu PARENT_MENU;

}
