package org.example.jakartabookstore.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.jakartabookstore.model.Emprestimo;
import org.example.jakartabookstore.service.EmprestimoService;

import java.util.List;

@Path("/emprestimos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmprestimoResource {

    @Inject
    private EmprestimoService emprestimoService;

    // DTO para o corpo da requisição de empréstimo
    public static class EmprestimoRequestDTO {
        public Long livroId;
        public Long usuarioId;
    }

    @GET
    @Path("/ativos")
    public Response listarEmprestimosAtivos() {
        List<Emprestimo> emprestimosAtivos = emprestimoService.listarEmprestimosAtivos();
        // Retorna 200 OK com a lista (que pode estar vazia)
        return Response.ok(emprestimosAtivos).build();
    }

    @POST
    @Path("/emprestar")
    public Response emprestar(EmprestimoRequestDTO request) {
        if (request.livroId == null || request.usuarioId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("livroId e usuarioId são obrigatórios.").build();
        }
        try {
            Emprestimo emprestimo = emprestimoService.emprestarLivro(request.livroId, request.usuarioId);
            return Response.status(Response.Status.CREATED).entity(emprestimo).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    // DTO para o corpo da requisição de devolução
    public static class DevolucaoRequestDTO {
        public Long livroId;
    }

    @POST
    @Path("/devolver")
    public Response devolver(DevolucaoRequestDTO request) {
        if (request.livroId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("livroId é obrigatório.").build();
        }
        try {
            Emprestimo emprestimo = emprestimoService.devolverLivro(request.livroId);
            return Response.ok(emprestimo).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }
}