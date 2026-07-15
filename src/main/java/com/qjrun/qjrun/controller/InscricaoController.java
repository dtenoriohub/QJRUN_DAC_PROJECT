package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.entity.Inscricao;
import com.qjrun.qjrun.service.InscricaoService;
import com.qjrun.qjrun.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscricoes")
@RequiredArgsConstructor
public class InscricaoController {

    private final InscricaoService inscricaoService;

    // CREATE (inscrever o aluno no evento)
    @PostMapping("/aluno/{alunoId}/evento/{eventoId}")
    public ResponseEntity<Inscricao> inscrever(
            @PathVariable Long alunoId,
            @PathVariable Long eventoId,
            @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO")  String perfilHeader,
            @RequestHeader(value = "Usuario-Id")  Long usuarioLogadoId) {

        AuthUtil.exigirAdminOuAluno(perfilHeader, usuarioLogadoId, alunoId);

        Inscricao novaInscricao = inscricaoService.inscrever(alunoId, eventoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaInscricao);
    }

    // READ (listar todas as inscrições ativas de um aluno específico)
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<Inscricao>> listarInscricoesDoAluno(
            @PathVariable Long alunoId,
            @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader,
            @RequestHeader(value = "Usuario-Id") Long usuarioLogadoId) {

        AuthUtil.exigirAdminOuAluno(perfilHeader, usuarioLogadoId, alunoId);

        List<Inscricao> inscricoes = inscricaoService.listarInscricoesDoAluno(alunoId);

        return ResponseEntity.ok(inscricoes);
    }

    // DELETE
    @DeleteMapping("/aluno/{alunoId}/evento/{eventoId}")
    public ResponseEntity<Void> cancelarInscricao(
            @PathVariable Long alunoId,
            @PathVariable Long eventoId,
            @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader,
            @RequestHeader(value = "Usuario-Id")   Long usuarioLogadoId) {

        AuthUtil.exigirAdminOuAluno(perfilHeader, usuarioLogadoId, alunoId);

        inscricaoService.cancelarInscricao(alunoId, eventoId);

        return ResponseEntity.noContent().build();
    }

    // Aprovação
    @PatchMapping("/{inscricaoId}/aprovar")
    public ResponseEntity<Void> aprovar(@PathVariable Long inscricaoId,
                                        @RequestHeader("Perfil-Usuario") String perfilHeader) {

        AuthUtil.exigirAdmin(perfilHeader);

        inscricaoService.aprovarInscricao(inscricaoId);
        return ResponseEntity.noContent().build();
    }

    // Listar todas as inscrições pro admin (pendentes no topo) com paginação
    @GetMapping
    public ResponseEntity<Page<Inscricao>> listarPendentes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Perfil-Usuario") String perfilHeader) {

        // ✨ Usando o seu AuthUtil
        AuthUtil.exigirAdmin(perfilHeader);

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(inscricaoService.listarPendentesParaAdmin(pageable));
    }
}