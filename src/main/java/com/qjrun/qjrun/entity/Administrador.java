package com.qjrun.qjrun.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Builder.Default
    @Column(nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "administrador")
    @JsonIgnore
    private List<Plano> planos = new ArrayList<>();

    @OneToMany(mappedBy = "administrador")
    @JsonIgnore
    private List<Evento> eventos = new ArrayList<>();
}
