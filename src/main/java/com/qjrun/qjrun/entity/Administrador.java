package com.qjrun.qjrun.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(unique = true)
    private String email;

    private String telefone;

    @Column(nullable = false)
    private boolean ativo = true;
}
