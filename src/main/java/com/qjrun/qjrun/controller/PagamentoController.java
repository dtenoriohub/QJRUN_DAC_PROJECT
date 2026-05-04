package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.entity.Pagamento;
import com.qjrun.qjrun.service.PagamentoService;
import com.qjrun.qjrun.util.AuthUtil;
import lombok.RequiredArgsConstructor;
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

    // READ (só o admin pode ver as finanças do clube)
    @GetMapping
    public ResponseEntity<List<Pagamento>> findAll(@RequestHeader(value = "Perfil-Usuario", defaultValue = "ROLE_ALUNO") String perfilHeader) {

        AuthUtil.exigirAdmin(perfilHeader);

        List<Pagamento> pagamentos = pagamentoService.findAll();
        return ResponseEntity.ok(pagamentos);
    }

    // READ BY ALUNO (o aluno pode ver as próprias faturas)
    @GetMapping("/aluno/{id}")
    public ResponseEntity<List<Pagamento>> findByAlunoId(@PathVariable Long id) {

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