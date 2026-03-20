package org.example.jakartabookstore.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.jakartabookstore.model.Livro;
import org.example.jakartabookstore.repository.EmprestimoRepository;
import org.example.jakartabookstore.repository.LivroRepository;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LivroService {

    @Inject private LivroRepository livroRepository;
    @Inject private EmprestimoRepository emprestimoRepository;

    @Transactional
    public Livro adicionar(Livro livro) {
        return livroRepository.salvar(livro);
    }

    public List<Livro> listar() {
        return livroRepository.listarTodos();
    }

    public Optional<Livro> buscar(Long id) {
        return livroRepository.buscarPorId(id);
    }

    @Transactional
    public boolean remover(Long id) {
        emprestimoRepository.buscarEmprestimoAtivoPorLivroId(id).ifPresent(e -> {
            throw new IllegalStateException("Não é possível remover um livro que está atualmente emprestado.");
        });
        return livroRepository.deletar(id);
    }

    @Transactional
    public Optional<Livro> atualizar(Long id, Livro livroAtualizado) {
        return livroRepository.buscarPorId(id).map(livroExistente -> {
            livroExistente.setTitulo(livroAtualizado.getTitulo());
            livroExistente.setAutor(livroAtualizado.getAutor());
            return livroRepository.salvar(livroExistente);
        });
    }
}