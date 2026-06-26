package com.qjrun.qjrun.service;


import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Plano;
import com.qjrun.qjrun.entity.Turma;
import com.qjrun.qjrun.enums.PerfilAcesso;
import com.qjrun.qjrun.enums.StatusPagamento;
import com.qjrun.qjrun.repository.*;
import com.qjrun.qjrun.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;
    private final TurmaRepository turmaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final UsuarioRepository usuarioRepository;


    // CREATE
    @Transactional
    public Aluno save(Aluno aluno, String perfilUsuario) {
        aluno.setId(null);
        aluno.setAtivo(true);

        aluno.setPerfilAcesso(PerfilAcesso.ROLE_ALUNO);

        validarEmailUnico(aluno.getEmail());

        vincularPlanoNaCriacao(aluno);
        vincularTurmaNaCriacao(aluno, perfilUsuario);

        return alunoRepository.save(aluno);
    }

    // READ
    public List<Aluno> findAll() {
        return alunoRepository.findAllByAtivoTrue();
    }

    // READ
    public Aluno findById(Long id) {
        return alunoRepository.findById(id).orElseThrow(()-> new RuntimeException("Aluno não encontrado!"));
    }

    // UPDATE
    @Transactional
    public Aluno update(Long id, Aluno alunoAtualizado, String perfilUsuario) {
        Aluno alunoExistente = findById(id);

        //Chama os metodos de atualização separadamente
        validarEAtualizarEmail(alunoAtualizado, alunoExistente);
        atualizarDadosBase(alunoAtualizado, alunoExistente);
        atualizarTurma(alunoAtualizado, alunoExistente, perfilUsuario);
        atualizarPlano(alunoAtualizado, alunoExistente, perfilUsuario);

        return alunoRepository.save(alunoExistente);
    }

    // DELETE
    @Transactional
    public void desativar(Long id, String perfilUsuario) {

        AuthUtil.exigirAdmin(perfilUsuario);

        Aluno aluno = findById(id);

        validarSeAlunoJaEstaDesativado(aluno);
        validarPendenciasFinanceirasParaCancelamento(aluno);

        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }

    // REGRAS DE ATUALIZAÇÃO

    private void validarEAtualizarEmail(Aluno alunoAtualizado, Aluno alunoExistente) {

        String novoEmail = alunoAtualizado.getEmail();

        if (novoEmail == null || novoEmail.isBlank() || novoEmail.equals(alunoExistente.getEmail())) {
            return;
        }

        if (usuarioRepository.findByEmail(novoEmail).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ops! O e-mail " + novoEmail + " já está sendo usado!");
        }

        alunoExistente.setEmail(novoEmail);
    }

    private void atualizarDadosBase(Aluno alunoAtualizado, Aluno alunoExistente) {

        alunoExistente.setNome(alunoAtualizado.getNome());
        alunoExistente.setTelefone(alunoAtualizado.getTelefone());
    }

    private void atualizarTurma(Aluno alunoAtualizado, Aluno alunoExistente, String perfilUsuario) {

        boolean tentouAtualizarTurma = alunoAtualizado.getTurma() != null && alunoAtualizado.getTurma().getId() != null;

        if ("ROLE_ALUNO".equals(perfilUsuario)) {
            Long idTurmaAtual = (alunoExistente.getTurma() != null) ? alunoExistente.getTurma().getId() : null;

            // Se ele tentou enviar uma turma e o ID for diferente da turma atual, dispara o erro
            if (tentouAtualizarTurma && !alunoAtualizado.getTurma().getId().equals(idTurmaAtual)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Acesso negado: Alunos não podem alterar a própria turma. Solicite a mudança à administração.");
            }

            return;
        }

        if (tentouAtualizarTurma) {
            Turma novaTurma = turmaRepository.findById(alunoAtualizado.getTurma().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nova turma não encontrada."));

            alunoExistente.setTurma(novaTurma);
        }
    }

    private void atualizarPlano(Aluno alunoAtualizado,  Aluno alunoExistente, String perfilUsuario) {

        if ("ROLE_ALUNO".equals(perfilUsuario)) {
            return;
        }

        if (alunoAtualizado.getPlano() != null && alunoAtualizado.getPlano().getId() != null) {
            Plano novoPlano = planoRepository.findById(alunoAtualizado.getPlano().getId()).orElseThrow(() -> new RuntimeException("Novo plano não encontrado."));

            alunoExistente.setPlano(novoPlano);
        }
    }

    // REGRAS DE CRIAÇÃO
    private void vincularPlanoNaCriacao(Aluno aluno) {

        if (aluno.getPlano() == null || aluno.getPlano().getId() == null) {
            throw new RuntimeException("O aluno deve estar vinculado a um plano.");
        }

        Plano plano = planoRepository.findById(aluno.getPlano().getId())
                .orElseThrow(() -> new RuntimeException("Plano não encontrado."));

        aluno.setPlano(plano);
    }

    private void vincularTurmaNaCriacao(Aluno aluno, String perfilUsuario) {

        boolean tentouEscolherTurma = aluno.getTurma() != null && aluno.getTurma().getId() != null;

        // Regra de negócio: se for o próprio aluno se cadastrando, a atribuição a uma turma é bloqueada (só o admin pode atribuir um aluno a uma turma)
        if ("ROLE_ALUNO".equals(perfilUsuario)) {
            if (tentouEscolherTurma) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Acesso negado: Alunos não podem escolher a própria turma no cadastro. Apenas o administrador faz essa atribuição.");
            }

            // Se ele mandou vazio (correto), garante que o banco salve como nulo e encerra
            aluno.setTurma(null);
            return;
        }

        // Cláusula de Guarda do Admin: Se não mandou turma (ou mandou sem ID), zera e sai
        if (!tentouEscolherTurma) {
            aluno.setTurma(null);
            return;
        }

        Turma turma = turmaRepository.findById(aluno.getTurma().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Turma não encontrada."));

        aluno.setTurma(turma);
    }

    private void validarEmailUnico(String email) {

        if (email != null && !email.isBlank() && usuarioRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ops! O e-mail " + email + " já está cadastrado no sistema!");
        }
    }

    // REGRAS DE INATIVAÇÃO
    private void validarSeAlunoJaEstaDesativado(Aluno aluno) {

        if (!aluno.getAtivo()) {
            throw new RuntimeException("Aluno já está desativado.");
        }
    }

    private void validarPendenciasFinanceirasParaCancelamento(Aluno aluno) {

        boolean possuiPendencia = pagamentoRepository.findByAlunoId(aluno.getId())
                .stream()
                .anyMatch(pagamento ->
                        pagamento.getStatus() == StatusPagamento.PENDENTE ||
                                pagamento.getStatus() == StatusPagamento.ATRASADO
                );

        if (possuiPendencia) {
            throw new RuntimeException("Não é possível cancelar matrícula com pagamentos pendentes ou atrasados.");
        }
    }
}
