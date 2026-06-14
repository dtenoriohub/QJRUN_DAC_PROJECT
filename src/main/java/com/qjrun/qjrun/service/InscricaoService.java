package com.qjrun.qjrun.service;

import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Evento;
import com.qjrun.qjrun.entity.Inscricao;
import com.qjrun.qjrun.repository.InscricaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscricaoService {

    private final InscricaoRepository inscricaoRepository;

    // services para reaproveitar as validações de busca já existentes
    private final AlunoService alunoService;
    private final EventoService eventoService;
    private final PagamentoService pagamentoService;

    // CREATE
    @Transactional
    public Inscricao inscrever(Long alunoId, Long eventoId) {

        Aluno aluno = alunoService.findById(alunoId);
        Evento evento = eventoService.findById(eventoId);

        validarRegrasDeInscricao(aluno, evento);

        Inscricao inscricao = Inscricao.builder()
                .aluno(aluno)
                .evento(evento)
                .dataInscricao(LocalDateTime.now())
                .build();

        return inscricaoRepository.save(inscricao);
    }

    // DELETE
    @Transactional
    public void cancelarInscricao(Long alunoId, Long eventoId) {

        Aluno aluno = alunoService.findById(alunoId);
        Evento evento = eventoService.findById(eventoId);

        // Busca a caixa (Optional) com a inscrição ativa
        Inscricao inscricao = inscricaoRepository.findByAlunoAndEventoAndAtivoTrue(aluno, evento).orElseThrow(() -> new RuntimeException("Este aluno não possui inscrição ativa neste evento."));

        // Inativa a inscrição
        inscricao.setAtivo(false);
        inscricaoRepository.save(inscricao);
    }

    // READ
    public List<Inscricao> listarInscricoesDoAluno(Long alunoId) {

        Aluno aluno = alunoService.findById(alunoId);
        return inscricaoRepository.findAllByAlunoAndAtivoTrue(aluno);
    }

    // REGRAS DE NEGÓCIO

    private void validarRegrasDeInscricao(Aluno aluno, Evento evento) {

        validarPendenciaFinanceira(aluno);
        validarDuplicidadeDeInscricao(aluno, evento);
        validarDisponibilidadeDeVagas(evento);
    }

    private void validarPendenciaFinanceira(Aluno aluno) {

        if(pagamentoService.existePagamentoAtrasado(aluno)) {
            throw new RuntimeException("Inscrição bloqueada: este aluno possui pagamentos pendentes.");
        }
    }

    private void validarDuplicidadeDeInscricao(Aluno aluno, Evento evento) {

        if (inscricaoRepository.existsByAlunoAndEventoAndAtivoTrue(aluno, evento)) {
            throw new RuntimeException("O aluno já está inscrito neste evento.");
        }
    }

    private void validarDisponibilidadeDeVagas(Evento evento) {

        long inscricoesAtuais = inscricaoRepository.countByEventoAndAtivoTrue(evento);

        if (inscricoesAtuais >= evento.getVagas()) {
            throw new RuntimeException("As vagas para este evento já estão esgotadas.");
        }
    }
}
