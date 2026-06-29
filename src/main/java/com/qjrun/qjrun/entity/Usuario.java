package com.qjrun.qjrun.entity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qjrun.qjrun.enums.PerfilAcesso;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String senha;

    private String telefone;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilAcesso perfilAcesso;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(perfilAcesso.name())
        );
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return senha;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}