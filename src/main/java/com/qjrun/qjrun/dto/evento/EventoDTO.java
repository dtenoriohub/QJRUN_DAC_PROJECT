package com.qjrun.qjrun.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventoDTO {
    private String nome;
    private String descricao;
    private String local;
    private LocalDate data;
    private LocalTime horario;
    private Integer vagas;
    private BigDecimal valor;
}