package com.qjrun.qjrun.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qjrun.qjrun.entity.Evento;
import com.qjrun.qjrun.entity.Plano;
import com.qjrun.qjrun.entity.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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
@Table(name = "administradores")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Administrador extends Usuario {

    @OneToMany(mappedBy = "administrador")
    @JsonIgnore
    @Builder.Default
    private List<Plano> planos = new ArrayList<>();

    @OneToMany(mappedBy = "administrador")
    @JsonIgnore
    @Builder.Default
    private List<Evento> eventos = new ArrayList<>();
}



