package com.qjrun.qjrun.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CadastroResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String mensagem;


}