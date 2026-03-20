package org.example.jakartabookstore.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.jakartabookstore.model.Emprestimo;
import org.example.jakartabookstore.model.Livro;
import org.example.jakartabookstore.model.Usuario;
import org.example.jakartabookstore.repository.EmprestimoRepository;
import org.example.jakartabookstore.repository.LivroRepository;
import org.example.jakartabookstore.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmprestimoService {

    @Inject private EmprestimoRepository emprestimoRepository;
    @Inject private LivroRepository livroRepository;
    @Inject private UsuarioRepository usuarioRepository;

    @Transactional
    public Emprestimo emprestarLivro(Long livroId, Long usuarioId) {
        Livro livro = livroRepository.buscarPorId(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro com ID " + livroId + " não encontrado."));
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com ID " + usuarioId + " não encontrado."));

        // Verifica se já existe um empréstimo ativo para este livro
        emprestimoRepository.buscarEmprestimoAtivoPorLivroId(livroId).ifPresent(e -> {
            throw new IllegalStateException("Livro '" + livro.getTitulo() + "' já está emprestado.");
        });

        Emprestimo novoEmprestimo = new Emprestimo();
        novoEmprestimo.setLivro(livro);
        novoEmprestimo.setUsuario(usuario);
        novoEmprestimo.setDataEmprestimo(LocalDate.now());
        novoEmprestimo.setDataDevolucaoPrevista(LocalDate.now().plusWeeks(2)); // Empréstimo de 2 semanas

        return emprestimoRepository.salvar(novoEmprestimo);
    }

    @Transactional
    public Emprestimo devolverLivro(Long livroId) {
        Emprestimo emprestimoAtivo = emprestimoRepository.buscarEmprestimoAtivoPorLivroId(livroId)
                .orElseThrow(() -> new IllegalStateException("Livro com ID " + livroId + " não está atualmente emprestado."));

        emprestimoAtivo.setDataDevolucaoReal(LocalDate.now());

        return emprestimoRepository.salvar(emprestimoAtivo);
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepository.listarEmprestimosAtivos();
    }
}