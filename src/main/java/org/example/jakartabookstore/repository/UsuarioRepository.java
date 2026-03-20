package org.example.jakartabookstore.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.jakartabookstore.model.Usuario;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            em.persist(usuario);
        } else {
            usuario = em.merge(usuario);
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Usuario.class, id));
    }

    @Transactional
    public boolean deletar(Long id) {
        Optional<Usuario> usuarioOpt = buscarPorId(id);
        if (usuarioOpt.isPresent()) {
            em.remove(usuarioOpt.get());
            return true;
        }
        return false;
    }
}