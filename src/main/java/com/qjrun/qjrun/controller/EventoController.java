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
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    // READ (Aberto: Alunos e Admins precisam ver os eventos)
    @GetMapping
    public ResponseEntity<List<Evento>> findAll() {
        List<Evento> eventos = eventoService.findAll();
        return ResponseEntity.ok(eventos);
    }

    // READ BY ID (Aberto: Alunos e Admins precisam ver os detalhes dos eventos)
    @GetMapping("/{id}")
    public ResponseEntity<Evento> findById(@PathVariable Long id) {
        Evento evento = eventoService.findById(id);
        return ResponseEntity.ok(evento);
    }

    // CREATE (Somente Administrador pode criar novas provas)
    @PostMapping
    public ResponseEntity<Evento> create(@RequestBody Evento evento, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO")  String perfilHeader, @RequestHeader(value = "Usuario-Id") Long usuarioId) {

        AuthUtil.exigirAdmin(perfilHeader);

        Evento eventoSalvo = eventoService.save(evento,  usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoSalvo);
    }

    // UPDATE (Somente Administrador pode fazer atualizações de eventos)
    @PutMapping("/{id}")
    public ResponseEntity<Evento> update(@PathVariable Long id, @RequestBody Evento evento, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO")   String perfilHeader) {

        AuthUtil.exigirAdmin(perfilHeader);

        Evento eventoSalvo = eventoService.update(id, evento);
        return ResponseEntity.ok(eventoSalvo);
    }

    // DELETE (Somente Administrador pode "deletar" um evento)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {

        AuthUtil.exigirAdmin(perfilHeader);

        eventoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}