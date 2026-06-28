package com.qjrun.qjrun.dto.mapper;

import com.qjrun.qjrun.dto.auth.CadastroRequestDTO;
import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.enums.PerfilAcesso;

public class AuthMapper {

    public static Aluno toAluno(CadastroRequestDTO dto) {

        Aluno aluno = new Aluno();

        aluno.setNome(dto.getNome());
        aluno.setCpf(dto.getCpf());
        aluno.setEmail(dto.getEmail());
        aluno.setTelefone(dto.getTelefone());
        aluno.setDataNascimento(dto.getDataNascimento());

        aluno.setPerfilAcesso(PerfilAcesso.ROLE_ALUNO);

        return aluno;

    }

}