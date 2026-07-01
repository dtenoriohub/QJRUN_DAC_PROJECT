package com.qjrun.qjrun.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardDTO {
    private long totalAlunos;
    private long totalPlanos;
    private long totalTurmas;
    private List<String> ultimosCadastros; // Lista de nomes
}