package com.qjrun.qjrun.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qjrun.qjrun.enums.NivelTurma;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelTurma nivelTurma;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalTime horarioInicio;

    @Column(nullable = false)
    private LocalTime horarioTermino;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    // CARDINALIDADE
    @OneToMany(mappedBy = "turma")
    @JsonIgnore
    private List<Aluno> alunos = new ArrayList<>();

}
