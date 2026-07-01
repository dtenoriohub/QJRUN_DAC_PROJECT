package com.qjrun.qjrun.entity;

import com.qjrun.qjrun.enums.StatusInscricao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscricao {

    @Enumerated(EnumType.STRING)
    private StatusInscricao status = StatusInscricao.PENDENTE;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataInscricao;

    @Column(nullable = false)
    private Boolean ativo = true;

    // RELACIONAMENTOS

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;
}
