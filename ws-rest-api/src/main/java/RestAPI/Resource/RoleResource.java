package RestAPI.Resource;

import RestAPI.Controller.RoleController;
import RestAPI.Entity.Role;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;

@Path("bitnet/role")
public class RoleResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private RoleController roleController;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles() {
        return Response.ok(
                this.getRoleController().getAllRoles(), MediaType.APPLICATION_JSON
        ).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRole(@PathParam("id") Integer id) {
        Response response;

        if (this.getRoleController().getRole(id).isPresent()) {
            Role toReturn = this.getRoleController().getRole(id).get();

            response = Response.ok(
                    toReturn, MediaType.APPLICATION_JSON
            ).build();
        } else {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity("The role with the ID: " + id + " wasn´t found int the database").build();
        }

        return response;
    }

    private RoleController getRoleController() {
        if (roleController == null) {
            roleController = new RoleController();
        }

        return roleController;
    }
}
