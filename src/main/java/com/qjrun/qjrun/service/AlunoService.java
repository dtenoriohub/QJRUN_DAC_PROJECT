package com.qjrun.qjrun.service;

import com.qjrun.qjrun.dto.aluno.AlunoResponseDTO;
import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Plano;
import com.qjrun.qjrun.entity.Turma;
import com.qjrun.qjrun.enums.PerfilAcesso;
import com.qjrun.qjrun.enums.StatusPagamento;
import com.qjrun.qjrun.repository.*;
import com.qjrun.qjrun.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
        aluno.setId(null); // Garante que seja um insert
        aluno.setAtivo(true);
        aluno.setPerfilAcesso(PerfilAcesso.ROLE_ALUNO);

        validarEmailUnico(aluno.getEmail());
        vincularPlanoNaCriacao(aluno);
        vincularTurmaNaCriacao(aluno, perfilUsuario);

        return alunoRepository.save(aluno);
    }
    public Page<AlunoResponseDTO> listarTodosDTO(Pageable pageable) {
        // Agora chama o repository passando o pageable
        // O próprio Page<> já possui um método .map(), dispensando o .stream() e o .collect()
        return alunoRepository.findAllByAtivoTrue(pageable)
                .map(a -> AlunoResponseDTO.builder()
                        .id(a.getId())
                        .nome(a.getNome())
                        .matricula(a.getMatricula())
                        .cpf(a.getCpf())
                        .email(a.getEmail())
                        .telefone(a.getTelefone())
                        .dataNascimento(a.getDataNascimento())
                        .ativo(a.getAtivo())
                        .plano(a.getPlano() != null ? a.getPlano().getTipo() : "Sem Plano")
                        .turma(a.getTurma() != null ? a.getTurma().getNome() : "Sem Turma")
                        .build());
    }

    // READ
    public Page<Aluno> findAll(Pageable pageable) {

        return alunoRepository.findAllByAtivoTrue(pageable);
    }

    public Aluno findById(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado!"));
    }

    // UPDATE
    @Transactional
    public Aluno update(Long id, Aluno alunoAtualizado, String perfilUsuario) {
        Aluno alunoExistente = findById(id);

        validarEAtualizarEmail(alunoAtualizado, alunoExistente);
        atualizarDadosBase(alunoAtualizado, alunoExistente);
        atualizarTurma(alunoAtualizado, alunoExistente, perfilUsuario);
        atualizarPlano(alunoAtualizado, alunoExistente);

        return alunoRepository.save(alunoExistente);
    }

    // DELETE (SOFT DELETE)
    @Transactional
    public void desativar(Long id, String perfilUsuario) {
        AuthUtil.exigirAdmin(perfilUsuario);
        Aluno aluno = findById(id);

        validarSeAlunoJaEstaDesativado(aluno);
        validarPendenciasFinanceirasParaCancelamento(aluno);

        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }

    // --- REGRAS PRIVADAS ---

    private void validarEAtualizarEmail(Aluno alunoAtualizado, Aluno alunoExistente) {
        if (alunoAtualizado.getEmail() == null || alunoAtualizado.getEmail().equals(alunoExistente.getEmail())) {
            return;
        }

        if (usuarioRepository.existsByEmail(alunoAtualizado.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já está em uso por outro usuário.");
        }
        alunoExistente.setEmail(alunoAtualizado.getEmail());
    }

    private void atualizarDadosBase(Aluno alunoAtualizado, Aluno alunoExistente) {
        alunoExistente.setNome(alunoAtualizado.getNome());
        alunoExistente.setTelefone(alunoAtualizado.getTelefone());
    }

    private void atualizarTurma(Aluno alunoAtualizado, Aluno alunoExistente, String perfilUsuario) {
        if (alunoAtualizado.getTurma() == null || alunoAtualizado.getTurma().getId() == null) return;

        if ("ROLE_ALUNO".equals(perfilUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Alunos não podem alterar a própria turma.");
        }

        Turma novaTurma = turmaRepository.findById(alunoAtualizado.getTurma().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Turma não encontrada."));

        alunoExistente.setTurma(novaTurma);
    }

    private void atualizarPlano(Aluno alunoAtualizado, Aluno alunoExistente) {
        if (alunoAtualizado.getPlano() != null && alunoAtualizado.getPlano().getId() != null) {
            Plano novoPlano = planoRepository.findById(alunoAtualizado.getPlano().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plano não encontrado."));
            alunoExistente.setPlano(novoPlano);
        }
    }

    private void vincularPlanoNaCriacao(Aluno aluno) {
        if (aluno.getPlano() == null || aluno.getPlano().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O aluno deve estar vinculado a um plano.");
        }
        Plano plano = planoRepository.findById(aluno.getPlano().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado."));
        aluno.setPlano(plano);
    }

    private void vincularTurmaNaCriacao(Aluno aluno, String perfilUsuario) {
        if ("ROLE_ALUNO".equals(perfilUsuario)) {
            aluno.setTurma(null);
            return;
        }

        if (aluno.getTurma() != null && aluno.getTurma().getId() != null) {
            Turma turma = turmaRepository.findById(aluno.getTurma().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Turma não encontrada."));
            aluno.setTurma(turma);
        }
    }

    private void validarEmailUnico(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
        }
    }

    private void validarSeAlunoJaEstaDesativado(Aluno aluno) {
        if (!aluno.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aluno já está desativado.");
        }
    }

    private void validarPendenciasFinanceirasParaCancelamento(Aluno aluno) {
        boolean possuiPendencia = pagamentoRepository.findByAlunoId(aluno.getId())
                .stream()
                .anyMatch(p -> p.getStatus() == StatusPagamento.PENDENTE || p.getStatus() == StatusPagamento.ATRASADO);

        if (possuiPendencia) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Não é possível inativar com pagamentos pendentes.");
        }
    }
}