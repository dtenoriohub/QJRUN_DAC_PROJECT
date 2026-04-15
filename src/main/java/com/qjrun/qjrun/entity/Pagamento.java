package com.qjrun.qjrun.entity;

import com.qjrun.qjrun.enums.StatusPagamento;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pagamentos")
public class Pagamento {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private BigDecimal valor;

        @Column(nullable = false)
        private String referencia; // Ex: Maio/2026

        @Column(nullable = false)
        private LocalDate vencimento;

        private LocalDate dataPagamento;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private StatusPagamento status;

        @Column(length = 1000)
        private String pixCopiaECola;

        @ManyToOne
        @JoinColumn(name = "aluno_id", nullable = false)
        private Aluno aluno;
    }

