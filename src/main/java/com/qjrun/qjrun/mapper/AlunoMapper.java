package com.qjrun.qjrun.mapper;

import com.qjrun.qjrun.dto.aluno.AlunoResponseDTO;
import com.qjrun.qjrun.entity.Aluno;

public class AlunoMapper {

    private AlunoMapper() {
    }

    public static AlunoResponseDTO toDTO(Aluno aluno) {

        return AlunoResponseDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .cpf(aluno.getCpf())
                .email(aluno.getEmail())
                .telefone(aluno.getTelefone())
                .matricula(aluno.getMatricula())
                .dataNascimento(aluno.getDataNascimento())
                .ativo(aluno.getAtivo())
                .plano(aluno.getPlano() != null ? aluno.getPlano().getTipo() : null)
                .turma(aluno.getTurma() != null ? aluno.getTurma().getNome() : null)
                .build();
    }

}