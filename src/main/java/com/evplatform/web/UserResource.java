package com.evplatform.web;

import com.evplatform.service.interfaces.UserServiceInterface;
import com.evplatform.vao.User;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/users") // Določa, da je ta vir dostopen na poti /api/users
@Produces(MediaType.APPLICATION_JSON) // Določa, da bodo vse metode vračale JSON format
@Consumes(MediaType.APPLICATION_JSON) // Določa, da ta vir sprejema podatke v JSON formatu
public class UserResource {

    @EJB
    private UserServiceInterface userService;

    /**
     * Metoda, ki odgovarja na HTTP GET zahteve na /api/users.
     * Vrne seznam vseh uporabnikov v sistemu.
     *
     * @return Seznam vseh uporabnikov kot JSON.
     */
    @GET
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @POST
    public Response addUser(User user) {
        try {
            userService.addUser(user);
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}