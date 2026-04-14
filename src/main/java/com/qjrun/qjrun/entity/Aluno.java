package com.qjrun.qjrun.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Alunos")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(unique = true)
    private String email;

    private String telefone;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo=true;

    // CARDINALIDADE
    @ManyToOne
    @JoinColumn(name = "plano_id", nullable = false)
    private Plano plano;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turma turma;
}
