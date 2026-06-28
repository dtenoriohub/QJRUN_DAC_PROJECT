package com.qjrun.qjrun.dto.aluno;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlunoRequestDTO {

    private String nome;

    private String cpf;

    private String email;

    private String telefone;

    private LocalDate dataNascimento;

    private Long planoId;

    private Long turmaId;

}