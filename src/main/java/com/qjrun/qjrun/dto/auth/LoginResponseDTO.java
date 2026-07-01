package com.qjrun.qjrun.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder // 🔑 Adicione esta anotação
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long id;
    private String nome;
    private String email;
    private String perfilAcesso;
}