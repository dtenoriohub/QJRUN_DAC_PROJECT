package com.qjrun.qjrun.dto.aluno;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AlunoResponseDTO {

    private Long id;

    private String nome;

    private String matricula;

    private String cpf;

    private String email;

    private String telefone;

    private LocalDate dataNascimento;

    private Boolean ativo;

    private String plano;

    private String turma;

}