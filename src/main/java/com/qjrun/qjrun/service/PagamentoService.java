package com.qjrun.qjrun.service;


import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Pagamento;
import com.qjrun.qjrun.enums.StatusPagamento;
import com.qjrun.qjrun.repository.AlunoRepository;
import com.qjrun.qjrun.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AlunoRepository alunoRepository;

    // CREATE (gerar nova cobrança/fatura)
    @Transactional
    public Pagamento create(Pagamento pagamento) {

        Aluno aluno = alunoRepository.findById(pagamento.getAluno().getId()).orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        pagamento.setAluno(aluno);
        pagamento.setStatus(StatusPagamento.PENDENTE);

        // Gerar código PEX simulado
        pagamento.setPixCopiaECola("PIX-QJRUN-" + aluno.getId() + "-" + pagamento.getReferencia());

        return pagamentoRepository.save(pagamento);
    }

    // READ
    public List<Pagamento> findAll() {
        return pagamentoRepository.findAll();
    }

    // READ (buscar as faturas de um aluno específico)
    public List<Pagamento> findByAlunoId(Long alunoId) {
        return pagamentoRepository.findByAlunoId(alunoId);
    }

    // VERIFICAR PAGAMENTOS EM ATRASO
    @Scheduled(fixedDelay = 10000) //executa a cada 10 segundos para fins de teste
    @Transactional
    public void atualizarPagamentosAtrasados() {
        List<Pagamento> pendentes = pagamentoRepository.findByStatus(StatusPagamento.PENDENTE);

        for (Pagamento pagamento : pendentes) {
            if (pagamento.getVencimento().isBefore(LocalDate.now())) {
                pagamento.setStatus(StatusPagamento.ATRASADO);
                pagamentoRepository.save(pagamento);
            }
        }

        System.out.println("Verificação de pagamentos em atraso executada!");
    }

    // CONFIRMAR PAGAMENTO
    @Transactional
    public Pagamento confirmar(Long id) {

        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pagamento não encontrado."));

        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(LocalDate.now());

        return pagamentoRepository.save(pagamento);
    }
}