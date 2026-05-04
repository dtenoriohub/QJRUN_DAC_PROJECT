package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    @GetMapping
    public List<Aluno> findAll() {
        return alunoService.findAll();
    }

    @GetMapping("/{id}")
    public Aluno findById(@PathVariable long id) {
        return alunoService.findById(id);
    }

    @PostMapping
    public Aluno create(@RequestBody Aluno aluno) {
        return alunoService.save(aluno);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        alunoService.desativar(id);
    }

    @PutMapping("/{id}")
    public Aluno update(@PathVariable Long id, @RequestBody Aluno aluno) {
        return alunoService.update(id, aluno);
    }

}