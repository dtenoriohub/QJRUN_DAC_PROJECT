package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.entity.Pagamento;
import com.qjrun.qjrun.service.PagamentoService;
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
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    // CREATE (só o admin gera cobranças)
    @PostMapping
    public ResponseEntity<Pagamento> create(
            @RequestBody Pagamento pagamento,
            @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {

        AuthUtil.exigirAdmin(perfilHeader);

        Pagamento salvarPagamento = pagamentoService.create(pagamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvarPagamento);
    }

    // READ Paginado (só o admin pode ver as finanças do clube)
    @GetMapping
    public ResponseEntity<Page<Pagamento>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {

        AuthUtil.exigirAdmin(perfilHeader);

        Pageable pageable = PageRequest.of(page, size);
        Page<Pagamento> pagamentosPaginados = pagamentoService.findAll(pageable);
        return ResponseEntity.ok(pagamentosPaginados);
    }

    // READ BY ALUNO (o aluno pode ver as próprias faturas)
    @GetMapping("/aluno/{id}")
    public ResponseEntity<List<Pagamento>> findByAlunoId(
            @PathVariable Long id,
            @RequestHeader(value = "Perfil-Usuario") String perfilHeader,
            @RequestHeader(value = "Usuario-Id") Long usuarioLogadoId) {

        AuthUtil.exigirAdminOuAluno(perfilHeader, usuarioLogadoId, id);

        // A listagem para o aluno continua trazendo tudo de uma vez (List normal),
        // pois um aluno não terá milhares de faturas sozinho.
        List<Pagamento> pagamentos = pagamentoService.findByAlunoId(id);
        return ResponseEntity.ok(pagamentos);
    }

    // CONFIRMAR PAGAMENTO (só o admin dá baixa)
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Pagamento> confirmar(@PathVariable Long id, @RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO")String perfilHeader) {

        AuthUtil.exigirAdmin(perfilHeader);

        Pagamento pagamentoConfirmado = pagamentoService.confirmar(id);
        return ResponseEntity.ok(pagamentoConfirmado);
    }
}