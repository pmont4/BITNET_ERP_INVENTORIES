package RestAPI.Resource;

import java.io.Serializable;

import RestAPI.Controller.UserController;
import RestAPI.Entity.User;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.NoArgsConstructor;

@Path("bitnet/user")
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

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Integer id) {
        Response response;

        if (this.getUserController().getUser(id).isPresent()) {
            User toReturn = this.getUserController().getUser(id).get();

            response = Response.ok(
                toReturn, MediaType.APPLICATION_JSON
            ).build();
        } else {
            response = Response.status(Status.NOT_FOUND)
                        .entity("The user with the ID: " + id + " wasn´t found int the database").build();
        }

        return response;
    }

    private UserController getUserController() {
        if (userController == null) {
            userController = new UserController();
        }

        return userController;
    }
}
