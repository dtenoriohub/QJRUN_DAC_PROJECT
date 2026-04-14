package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.entity.Evento;
import com.qjrun.qjrun.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evento")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    public List<Evento> listar() {
        return eventoService.listar();
    }

    @GetMapping("/{id}")
    public Evento buscar(@PathVariable Long id) {
        return eventoService.buscar(id);
    }

    @PostMapping
    public Evento criar(@RequestBody Evento evento) {
        return eventoService.criar(evento);
    }

    @PutMapping("/{id}")
    public Evento atualizar(@PathVariable Long id, @RequestBody Evento evento) {
        return eventoService.atualizar(id, evento);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        eventoService.deletar(id);
    }
}