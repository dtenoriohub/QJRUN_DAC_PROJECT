package com.qjrun.qjrun.mapper;

import com.qjrun.qjrun.dto.auth.CadastroRequestDTO;
import com.qjrun.qjrun.dto.auth.LoginResponseDTO;
import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Usuario;

public class AuthMapper {

    private AuthMapper() {
    }

    public static Aluno toAluno(CadastroRequestDTO dto) {

        return Aluno.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .dataNascimento(dto.getDataNascimento())
                .build();

    }

    public static LoginResponseDTO toLoginResponse(
            Usuario usuario,
            String token
    ) {

        return LoginResponseDTO.builder()
                .token(token)
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfilAcesso(usuario.getPerfilAcesso().name())
                .build();

    }

}