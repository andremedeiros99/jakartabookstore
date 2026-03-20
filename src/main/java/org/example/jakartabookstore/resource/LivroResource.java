package org.example.jakartabookstore.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.jakartabookstore.model.Livro;
import org.example.jakartabookstore.service.LivroService;
import java.util.List;

@Path("/livros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LivroResource {

    @Inject private LivroService livroService;

    @GET
    public List<Livro> listar() {
        return livroService.listar();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        return livroService.buscar(id)
                .map(livro -> Response.ok(livro).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response adicionar(Livro livro) {
        Livro novoLivro = livroService.adicionar(livro);
        return Response.status(Response.Status.CREATED).entity(novoLivro).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Livro livroAtualizado) {
        return livroService.atualizar(id, livroAtualizado)
                .map(livro -> Response.ok(livro).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {
        try {
            if (livroService.remover(id)) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }
}