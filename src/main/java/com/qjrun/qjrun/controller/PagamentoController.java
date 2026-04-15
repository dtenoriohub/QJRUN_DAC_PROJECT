package com.qjrun.qjrun.controller;


import com.qjrun.qjrun.entity.Pagamento;
import com.qjrun.qjrun.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamento")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @GetMapping
    public List<Pagamento> listar() {
        return pagamentoService.listar();
    }

    @PostMapping
    public Pagamento criar(@RequestBody Pagamento pagamento) {
        return pagamentoService.criar(pagamento);
    }

    @PutMapping("/{id}/confirmar")
    public Pagamento confirmar(@PathVariable Long id) {
        return pagamentoService.confirmar(id);
    }

    @GetMapping("/aluno/{id}")
    public List<Pagamento> buscarPorAluno(@PathVariable Long id) {
        return pagamentoService.buscarPorAluno(id);
    }
}


