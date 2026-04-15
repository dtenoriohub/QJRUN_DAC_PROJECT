package com.qjrun.qjrun.service;


import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Pagamento;
import com.qjrun.qjrun.enums.StatusPagamento;
import com.qjrun.qjrun.repository.AlunoRepository;
import com.qjrun.qjrun.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AlunoRepository alunoRepository;

    public List<Pagamento> listar() {

        List<Pagamento> lista = pagamentoRepository.findAll();

        for (Pagamento p : lista) {

            if (p.getStatus() == StatusPagamento.PENDENTE &&
                    p.getVencimento().isBefore(LocalDate.now())) {

                p.setStatus(StatusPagamento.ATRASADO);
                pagamentoRepository.save(p);
            }
        }

        return lista;
    }

    public List<Pagamento> buscarPorAluno(Long alunoId) {
        return pagamentoRepository.findByAlunoId(alunoId);
    }

    public Pagamento criar(Pagamento pagamento) {

        Aluno aluno = alunoRepository.findById(
                pagamento.getAluno().getId()
        ).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        pagamento.setAluno(aluno);
        pagamento.setStatus(StatusPagamento.PENDENTE);

        pagamento.setPixCopiaECola(
                "PIX-QJRUN-" + aluno.getId() + "-" + pagamento.getReferencia()
        );

        return pagamentoRepository.save(pagamento);
    }

    public Pagamento confirmar(Long id) {

        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(LocalDate.now());

        return pagamentoRepository.save(pagamento);
    }
}


