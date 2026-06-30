package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.entity.Plano;
import com.qjrun.qjrun.service.PlanoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos")
@RequiredArgsConstructor
public class PlanoController {

    private final PlanoService planoService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Plano novoPlano,
            @RequestHeader(value = "Perfil-Usuario", required = false) String perfilHeader
    ) {
        // 🔐 Segurança rígida para CRIAÇÃO: Apenas ADMIN
        if (!"ROLE_ADMIN".equals(perfilHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acesso negado: Apenas administradores podem criar planos.");
        }

        if (novoPlano.getTipo() != null) {
            novoPlano.setTipo(novoPlano.getTipo().toUpperCase());
        }

        novoPlano.setAtivo(true);
        Plano planoSalvo = planoService.save(novoPlano);
        return ResponseEntity.status(HttpStatus.CREATED).body(planoSalvo);
    }

    @GetMapping
    public List<Plano> findAll(
            // 🔑 Adicionado aqui para o Spring aceitar o header enviado pelo interceptor do Axios sem dar 403
            @RequestHeader(value = "Perfil-Usuario", required = false) String perfilHeader
    ) {
        // O GET permanece livre para qualquer usuário autenticado (Admin ou Aluno)
        return planoService.findAll();
    }

    @GetMapping("/{id}")
    public Plano findById(
            @PathVariable Long id,
            @RequestHeader(value = "Perfil-Usuario", required = false) String perfilHeader
    ) {
        return planoService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Plano dadosAtualizados,
            @RequestHeader(value = "Perfil-Usuario", required = false) String perfilHeader
    ) {
        // 🔐 Segurança para ALTERAÇÃO: Apenas ADMIN
        if (!"ROLE_ADMIN".equals(perfilHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(planoService.update(id, dadosAtualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @RequestHeader(value = "Perfil-Usuario", required = false) String perfilHeader
    ) {
        // 🔐 Segurança para EXCLUSÃO: Apenas ADMIN
        if (!"ROLE_ADMIN".equals(perfilHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        planoService.desativar(id);
        return ResponseEntity.ok().build();
    }
}