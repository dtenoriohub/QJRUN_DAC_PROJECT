package com.qjrun.qjrun.controller;

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

    // READ (só o administrador vê a lista de alunos)
    @GetMapping
    public ResponseEntity<List<Aluno>> findAll(@RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {

        AuthUtil.exigirAdmin(perfilHeader);

        List<Aluno> alunos = alunoService.findAll();
        return ResponseEntity.ok(alunos);
    }

    // READ BY ID (o aluno pode ver o próprio perfil)
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> findById(@PathVariable Long id) {

        Aluno aluno = alunoService.findById(id);
        return ResponseEntity.ok(aluno);
    }

    // CREATE (o aluno pode se matricular)
    @PostMapping
    public ResponseEntity<Aluno> save(@RequestBody Aluno aluno, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE-aluno") String perfilHeader) {

        Aluno alunoSalvo = alunoService.save(aluno, perfilHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoSalvo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        alunoService.desativar(id);
    }

    //UPDATE (o aluno pode atualizar alguns dos próprios dados)
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> update(@PathVariable Long id, @RequestBody Aluno aluno) {

        Aluno alunoAtualizado = alunoService.update(id, aluno);
        return ResponseEntity.ok(alunoAtualizado);
    }
}