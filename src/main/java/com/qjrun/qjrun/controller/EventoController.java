package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.entity.Evento;
import com.qjrun.qjrun.service.EventoService;
import com.qjrun.qjrun.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // ALTERADO
    @PostMapping
    public ResponseEntity<Evento> create(@RequestBody Evento evento, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO")  String perfilHeader) {

        AuthUtil.exigirAdmin(perfilHeader);
        Evento eventoSalvo = eventoService.create(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoSalvo);
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