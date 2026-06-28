package com.qjrun.qjrun.dto.auth;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CadastroRequestDTO {

    private String nome;

    private String cpf;

    private String email;

    private String telefone;

    private LocalDate dataNascimento;

    private String senha;

}