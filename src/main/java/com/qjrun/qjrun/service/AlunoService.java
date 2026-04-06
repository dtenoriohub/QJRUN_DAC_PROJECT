package com.qjrun.qjrun.service;


import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }
    public Aluno save(Aluno aluno) {
        return alunoRepository.save(aluno);
    }
    public Aluno findById(long id) {
        return alunoRepository.findById(id).orElseThrow(()-> new RuntimeException("aluno não encontrado!"));
    }
    public void delete(long id) {
        alunoRepository.deleteById(id);
    }
    public Aluno update(Long id, Aluno alunoAtualizado) {
        Aluno aluno = findById(id);

        aluno.setNome(alunoAtualizado.getNome());
        aluno.setEmail(alunoAtualizado.getEmail());
        aluno.setTelefone(alunoAtualizado.getTelefone());
        aluno.setDataNascimento(alunoAtualizado.getDataNascimento());

        return alunoRepository.save(aluno);
    }
}
