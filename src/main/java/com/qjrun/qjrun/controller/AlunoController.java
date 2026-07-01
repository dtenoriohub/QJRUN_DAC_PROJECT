package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.dto.aluno.AlunoResponseDTO;
import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.service.AlunoService;
import com.qjrun.qjrun.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;


    // READ BY ID (o aluno pode ver o próprio perfil)
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> findById(@PathVariable Long id) {
        Aluno aluno = alunoService.findById(id);
        return ResponseEntity.ok(aluno);
    }

    // CREATE (o aluno pode se matricular ou admin cadastrar)
    @PostMapping
    public ResponseEntity<Aluno> save(@RequestBody Aluno aluno, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {
        Aluno alunoSalvo = alunoService.save(aluno, perfilHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoSalvo);
    }

    // DELETE (Inativação lógica do Aluno)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {
        alunoService.desativar(id, perfilHeader);
        return ResponseEntity.noContent().build();
    }

    // 🎯 UPDATE UNIFICADO (Suporta a troca de plano pelo Aluno e Edições pelo Admin)
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> update(
            @PathVariable Long id,
            @RequestBody Aluno aluno,
            @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader,
            @RequestHeader(value = "Usuario-Id", required = false) Long usuarioId
    ) {
        // Se o cabeçalho 'Usuario-Id' não for enviado (ex: requisições mais simples),
        // ou se for o admin editando, a validação não quebrará o Spring Boot
        if (usuarioId != null) {
            AuthUtil.exigirAdminOuAluno(perfilHeader, usuarioId, id);
        }

        // Executa a atualização aplicando as regras dinâmicas que alteramos no AlunoService
        Aluno alunoAtualizado = alunoService.update(id, aluno, perfilHeader);
        return ResponseEntity.ok(alunoAtualizado);
    }
    @GetMapping
    public List<AlunoResponseDTO> listar() {
        return alunoService.listarTodosDTO();
    }
}
