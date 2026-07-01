package com.qjrun.qjrun.dto.turma;

import com.qjrun.qjrun.enums.NivelTurma;
import lombok.Data;
import java.time.LocalTime;

@Data
public class TurmaRequestDTO {
    private String nome;
    private NivelTurma nivelTurma;
    private LocalTime horarioInicio;
    private LocalTime horarioTermino;
}