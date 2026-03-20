package org.example.jakartabookstore.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.jakartabookstore.model.Usuario;
import org.example.jakartabookstore.service.UsuarioService;
import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject private UsuarioService usuarioService;

    @GET
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        return usuarioService.buscar(id)
                .map(usuario -> Response.ok(usuario).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response adicionar(Usuario usuario) {
        Usuario novoUsuario = usuarioService.adicionar(usuario);
        return Response.status(Response.Status.CREATED).entity(novoUsuario).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Usuario usuarioAtualizado) {
        return usuarioService.atualizar(id, usuarioAtualizado)
                .map(usuario -> Response.ok(usuario).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {
        if (usuarioService.remover(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}