package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.entity.Administrador;
import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.service.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrador")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;

    @GetMapping
    public List<Administrador> findAll() {
        return administradorService.findAll();
    }

    @GetMapping("/{id}")
    public  Administrador findById(@PathVariable Long id) {
        return administradorService.findById(id);
    }

    @PostMapping
    public Administrador save(@RequestBody Administrador administrador) {
        return administradorService.save(administrador);
    }

    @PutMapping("/{id}")
    public Administrador update(@PathVariable Long id, @RequestBody Administrador administrador) {
        return administradorService.update(id, administrador);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        administradorService.inativar(id);
    }
}
