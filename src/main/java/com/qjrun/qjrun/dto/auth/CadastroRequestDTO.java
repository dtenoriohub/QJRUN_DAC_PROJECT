package com.qjrun.qjrun.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat; // 🔑 Importante adicionar o import

import java.time.LocalDate;

@Data
public class CadastroRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    private String telefone;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // 👈 🔑 ISSO DIZ AO SPRING PARA ACEITAR O FORMATO "YYYY-MM-DD" DO REACT
    private LocalDate dataNascimento;

}