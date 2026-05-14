package com.qjrun.qjrun.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Planos")
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer duracaoMeses;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    // CARDINALIDADE
    @OneToMany(mappedBy = "plano")
    @JsonIgnore
    private List<Aluno> alunos = new ArrayList<>();

    @OneToMany(mappedBy = "plano")
    @JsonIgnore
    private List<Pagamento> pagamentos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "administrador_id", nullable = false)
    private Administrador administrador;


}
