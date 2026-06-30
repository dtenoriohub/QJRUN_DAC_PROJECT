package com.qjrun.qjrun.dto.plano;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlanoRequestDTO(
        @NotBlank String tipo,
        @NotBlank String descricao,
        @NotNull Double preco,
        @NotNull Integer duracaoMeses
) {}