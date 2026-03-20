package org.example.jakartabookstore.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.jakartabookstore.model.Emprestimo;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmprestimoRepository {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager em;

    @Transactional
    public Emprestimo salvar(Emprestimo emprestimo) {
        if (emprestimo.getId() == null) {
            em.persist(emprestimo);
        } else {
            emprestimo = em.merge(emprestimo);
        }
        return emprestimo;
    }

    public Optional<Emprestimo> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Emprestimo.class, id));
    }

    public Optional<Emprestimo> buscarEmprestimoAtivoPorLivroId(Long livroId) {
        return em.createQuery(
                        "SELECT e FROM Emprestimo e WHERE e.livro.id = :livroId AND e.dataDevolucaoReal IS NULL", Emprestimo.class)
                .setParameter("livroId", livroId)
                .getResultStream()
                .findFirst();
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return em.createQuery(
                        "SELECT e FROM Emprestimo e WHERE e.dataDevolucaoReal IS NULL", Emprestimo.class)
                .getResultList();
    }
}