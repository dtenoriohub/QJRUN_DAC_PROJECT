package com.qjrun.qjrun.dto.turma;

import com.qjrun.qjrun.enums.NivelTurma;
import lombok.Builder;
import lombok.Data;
import java.time.LocalTime;

@Data
@Builder
public class TurmaResponseDTO {
    private Long id;
    private String nome;
    private NivelTurma nivelTurma;
    private LocalTime horarioInicio;
    private LocalTime horarioTermino;
    private Boolean ativo;
    private Integer quantidadeAlunosAtuais;
}