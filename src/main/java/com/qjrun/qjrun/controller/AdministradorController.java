package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.dto.auth.CadastroRequestDTO;
import com.qjrun.qjrun.entity.Administrador;
import com.qjrun.qjrun.service.AdministradorService;
import com.qjrun.qjrun.util.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrador")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // 🔓 Permite que o seu React acesse este Controller
public class AdministradorController {

    private final AdministradorService administradorService;

    // READ
    @GetMapping
    public ResponseEntity<List<Administrador>> findAll(@RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {
        AuthUtil.exigirAdmin(perfilHeader);
        List<Administrador> administradores = administradorService.findAll();
        return ResponseEntity.ok(administradores);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Administrador> findById(@PathVariable Long id, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {
        AuthUtil.exigirAdmin(perfilHeader);
        Administrador administrador = administradorService.findById(id);
        return ResponseEntity.ok(administrador);
    }

    // CREATE (Alterado para receber o DTO)
    @PostMapping
    public ResponseEntity<Administrador> save(@Valid @RequestBody CadastroRequestDTO dto, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {
        Administrador administradorSalvo = administradorService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(administradorSalvo);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Administrador> update(@PathVariable Long id, @RequestBody Administrador administrador, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {
        AuthUtil.exigirAdmin(perfilHeader);
        Administrador administradorAtualizado = administradorService.update(id, administrador);
        return ResponseEntity.ok(administradorAtualizado);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {
        AuthUtil.exigirAdmin(perfilHeader);
        administradorService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}