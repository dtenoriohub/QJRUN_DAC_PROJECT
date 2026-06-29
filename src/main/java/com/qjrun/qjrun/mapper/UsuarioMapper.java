package com.qjrun.qjrun.mapper;

import com.qjrun.qjrun.dto.auth.CadastroRequestDTO;
import com.qjrun.qjrun.entity.Aluno;

public class UsuarioMapper {

    public static Aluno toAluno(CadastroRequestDTO dto) {

        Aluno aluno = new Aluno();

        aluno.setNome(dto.getNome());
        aluno.setCpf(dto.getCpf());
        aluno.setEmail(dto.getEmail());
        aluno.setTelefone(dto.getTelefone());
        aluno.setDataNascimento(dto.getDataNascimento());

        return aluno;
    }

}