package com.qjrun.qjrun.service;

import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Inscricao;
import com.qjrun.qjrun.entity.Pagamento;
import com.qjrun.qjrun.enums.StatusPagamento;
import com.qjrun.qjrun.enums.TipoPagamento;
import com.qjrun.qjrun.repository.AlunoRepository;
import com.qjrun.qjrun.repository.PagamentoRepository;
import org.springframework.context.annotation.Lazy; // ⬅️ NOVO IMPORT
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
// Remoção do @RequiredArgsConstructor para poder usar o @Lazy no construtor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AlunoRepository alunoRepository;
    private final InscricaoService inscricaoService; // NOVA DEPENDÊNCIA

    // CONSTRUTOR MANUAL COM @LAZY (Evita o erro de Dependência Circular)
    public PagamentoService(PagamentoRepository pagamentoRepository,
                            AlunoRepository alunoRepository,
                            @Lazy InscricaoService inscricaoService) {
        this.pagamentoRepository = pagamentoRepository;
        this.alunoRepository = alunoRepository;
        this.inscricaoService = inscricaoService;
    }

    // CREATE (gerar nova cobrança/fatura)
    @Transactional
    public Pagamento create(Pagamento pagamento) {

        Aluno aluno = validarEBuscarAluno(pagamento.getAluno().getId());

        pagamento.setAluno(aluno);
        pagamento.setStatus(StatusPagamento.PENDENTE);

        if (pagamento.getTipoPagamento() == TipoPagamento.PLANO) {
            configurarPagamentoDePlano(pagamento, aluno);
        } else if (pagamento.getTipoPagamento() == TipoPagamento.INSCRICAO) {
            configurarPagamentoDeInscricao(pagamento, aluno);
        } else {
            throw new RuntimeException("Tipo de pagamento inválido ou não informado.");
        }

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

    // VERIFICAR SE O ALUNO ESTÁ INADIMPLENTE
    public boolean existePagamentoAtrasado(Aluno aluno) {
        return pagamentoRepository.existsByAlunoAndStatus(aluno, StatusPagamento.ATRASADO);
    }

    // VERIFICAR PAGAMENTOS EM ATRASO
    @Scheduled(fixedDelay = 600000)
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

    // CONFIRMAR PAGAMENTO (E APROVAR INSCRIÇÃO)
    @Transactional
    public Pagamento confirmar(Long id) {

        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado."));

        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(LocalDate.now());

        // Se for o pagamento de um evento, aprova automaticamente!
        if (pagamento.getTipoPagamento() == TipoPagamento.INSCRICAO && pagamento.getInscricao() != null) {
            inscricaoService.aprovarInscricao(pagamento.getInscricao().getId());
        }

        return pagamentoRepository.save(pagamento);
    }

    // MÉTODOS AUXILIARES
    private Aluno validarEBuscarAluno(Long alunoId) {

        Aluno aluno = alunoRepository.findById(alunoId).orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        if (!aluno.getAtivo()) {
            throw new RuntimeException("Não é possível gerar cobrança para um aluno inativo.");
        }

        return aluno;
    }

    private void configurarPagamentoDePlano(Pagamento pagamento, Aluno aluno) {

        if (aluno.getPlano() == null) {
            throw new RuntimeException("O aluno não possui um plano vinculado para gerar a mensalidade.");
        }

        pagamento.setPlano(aluno.getPlano());
        pagamento.setInscricao(null);
        pagamento.setPixCopiaECola("PIX-QJRUN-PLANO-" + aluno.getId() + "-" + pagamento.getReferencia());
    }

    private void configurarPagamentoDeInscricao(Pagamento pagamento, Aluno aluno) {

        if (pagamento.getInscricao() == null || pagamento.getInscricao().getId() == null) {
            throw new RuntimeException("Para gerar a taxa de um evento, a inscrição correspondente precisa ser informada.");
        }

        pagamento.setPlano(null);
        pagamento.setPixCopiaECola("PIX-QJRUN-INSC-" + aluno.getId() + "-" + pagamento.getInscricao().getId());
    }

    @Transactional
    public void cancelarPagamentosPendentesDaInscricao(Inscricao inscricao) {

        List<Pagamento> pagamentosDaInscricao = pagamentoRepository.findByInscricao(inscricao);

        for (Pagamento pagamento : pagamentosDaInscricao) {
            if (pagamento.getStatus() == StatusPagamento.PENDENTE) {
                pagamento.setStatus(StatusPagamento.CANCELADO);
                pagamentoRepository.save(pagamento);
            }
        }
    }
}