package org.example.jakartabookstore.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.jakartabookstore.model.Livro;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LivroRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    @Transactional
    public Livro salvar(Livro livro) {
        if (livro.getId() == null) {
            em.persist(livro);
        } else {
            livro = em.merge(livro);
        }
        return livro;
    }

    public List<Livro> listarTodos() {
        return em.createQuery("SELECT l FROM Livro l", Livro.class).getResultList();
    }

    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Livro.class, id));
    }

    @Transactional
    public boolean deletar(Long id) {
        Optional<Livro> livroOpt = buscarPorId(id);
        if (livroOpt.isPresent()) {
            em.remove(livroOpt.get());
            return true;
        }
        return false;
    }
}