package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.dto.EventoDTO;
import com.qjrun.qjrun.entity.Evento;
import com.qjrun.qjrun.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class EventoController {

    private final EventoService service;

    @GetMapping
    public List<Evento> listar() { return service.listar(); }

    @PostMapping
    public Evento criar(@RequestBody EventoDTO dto) { return service.salvar(dto); }

    @PutMapping("/{id}")
    public Evento atualizar(@PathVariable Long id, @RequestBody EventoDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}