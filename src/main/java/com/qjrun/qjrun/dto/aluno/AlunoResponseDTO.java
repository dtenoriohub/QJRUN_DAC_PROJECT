package com.qjrun.qjrun.dto.aluno;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC) // Force o construtor a ser público
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
    private String plano;  // Será uma String (ex: "Mensal")
    private String turma;  // Será uma String (ex: "Turma A")
}