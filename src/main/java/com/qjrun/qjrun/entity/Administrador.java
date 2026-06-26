package com.qjrun.qjrun.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Administrador extends Usuario {

    @OneToMany(mappedBy = "administrador")
    @JsonIgnore
    private List<Plano> planos = new ArrayList<>();

    @OneToMany(mappedBy = "administrador")
    @JsonIgnore
    private List<Evento> eventos = new ArrayList<>();
}
