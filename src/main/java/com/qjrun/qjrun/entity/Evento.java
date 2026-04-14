package com.qjrun.qjrun.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private String local;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Column(nullable = false)
    private Integer vagas;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToMany
    @JoinTable(
            name = "evento_aluno",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private List<Aluno> inscritos;
}



