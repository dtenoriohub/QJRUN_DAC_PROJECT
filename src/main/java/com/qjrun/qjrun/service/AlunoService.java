package com.qjrun.qjrun.service;


import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Plano;
import com.qjrun.qjrun.entity.Turma;
import com.qjrun.qjrun.repository.AlunoRepository;
import com.qjrun.qjrun.repository.PlanoRepository;
import com.qjrun.qjrun.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;
    private final TurmaRepository turmaRepository;

    // CREATE
    @Transactional
    public Aluno save(Aluno aluno) {

        if (aluno.getPlano() == null || aluno.getPlano().getId() == null) {
            throw new RuntimeException("O aluno deve estar vinculado a um plano.");
        }

        Plano plano = planoRepository.findById(aluno.getPlano().getId())
                .orElseThrow(() -> new RuntimeException("Plano não encontrado."));

        aluno.setPlano(plano);

        if (aluno.getTurma() != null && aluno.getTurma().getId() != null) {
            Turma turma = turmaRepository.findById(aluno.getTurma().getId())
                    .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
            aluno.setTurma(turma);
        } else {
            aluno.setTurma(null);
        }

        aluno.setAtivo(true);

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
    public Aluno update(Long id, Aluno alunoAtualizado) {
        Aluno alunoExistente = findById(id);

        //Chama os metodos de atualização separadamente
        validarEAtualizarEmail(alunoAtualizado, alunoExistente);
        atualizarDadosBase(alunoAtualizado, alunoExistente);
        atualizarTurma(alunoAtualizado, alunoExistente);
        atualizarPlano(alunoAtualizado, alunoExistente);

        return alunoRepository.save(alunoExistente);
    }

    // DELETE
    public void desativar(Long id) {
        Aluno aluno = findById(id);
        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }

    private void validarEAtualizarEmail(Aluno alunoAtualizado, Aluno alunoExistente) {

        String novoEmail = alunoAtualizado.getEmail();

        if (novoEmail != null && !novoEmail.equals(alunoExistente.getEmail())) {

            if (alunoRepository.findByEmail(novoEmail).isPresent()) {
                throw new RuntimeException("Ops! O e-mail " + novoEmail + " já está sendo usado!");
            }

            alunoExistente.setEmail(novoEmail);
        }
    }

    //MÉTODOS DE ATUALIZAÇÃO
    private void atualizarDadosBase(Aluno alunoAtualizado, Aluno alunoExistente) {

        alunoExistente.setNome(alunoAtualizado.getNome());
        alunoExistente.setTelefone(alunoAtualizado.getTelefone());
    }

    private void atualizarTurma(Aluno alunoAtualizado, Aluno alunoExistente) {

        if (alunoAtualizado.getTurma() != null && alunoAtualizado.getTurma().getId() != null) {
            Turma novaTurma = turmaRepository.findById(alunoAtualizado.getTurma().getId()).orElseThrow(() -> new RuntimeException("Nova turma não encontrada."));

            alunoExistente.setTurma(novaTurma);
        }
    }

    private void atualizarPlano(Aluno alunoAtualizado,  Aluno alunoExistente) {

        if (alunoAtualizado.getPlano() != null && alunoAtualizado.getPlano().getId() != null) {
            Plano novoPlano = planoRepository.findById(alunoAtualizado.getPlano().getId()).orElseThrow(() -> new RuntimeException("Novo plano não encontrado."));

            alunoExistente.setPlano(novoPlano);
        }
    }
}
