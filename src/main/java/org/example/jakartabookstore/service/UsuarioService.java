package org.example.jakartabookstore.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.jakartabookstore.model.Usuario;
import org.example.jakartabookstore.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioService {

    @Inject
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario adicionar(Usuario usuario) {
        return usuarioRepository.salvar(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.listarTodos();
    }

    public Optional<Usuario> buscar(Long id) {
        return usuarioRepository.buscarPorId(id);
    }

    @Transactional
    public boolean remover(Long id) {
        // Regra de negócio: idealmente, verificar se o usuário tem empréstimos ativos antes de remover.
        // Por simplicidade, essa verificação foi omitida, mas seria importante em um sistema real.
        return usuarioRepository.deletar(id);
    }

    @Transactional
    public Optional<Usuario> atualizar(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.buscarPorId(id).map(usuarioExistente -> {
            usuarioExistente.setNome(usuarioAtualizado.getNome());
            usuarioExistente.setEmail(usuarioAtualizado.getEmail());
            return usuarioRepository.salvar(usuarioExistente);
        });
    }
}