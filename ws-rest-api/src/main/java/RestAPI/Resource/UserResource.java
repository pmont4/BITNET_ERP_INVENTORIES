package RestAPI.Resource;

import java.io.Serializable;

import RestAPI.Controller.UserController;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.NoArgsConstructor;

@Path("bitnet/users")
@NoArgsConstructor
public class UserResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserController userController;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        return Response.ok(
            this.getUserController().getAllUsers(), MediaType.APPLICATION_JSON
        ).build();
    }
    
    private UserController getUserController() {
        if (userController == null) {
            userController = new UserController();
        }

        return userController;
    }
}
