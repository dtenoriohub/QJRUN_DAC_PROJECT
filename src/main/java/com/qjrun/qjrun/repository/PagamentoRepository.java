package com.qjrun.qjrun.repository;

import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Inscricao;
import com.qjrun.qjrun.entity.Pagamento;
import com.qjrun.qjrun.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByAlunoId(Long alunoId);

    List<Pagamento> findByStatus(StatusPagamento status);

    boolean existsByAlunoAndStatus(Aluno aluno, StatusPagamento status);

    List<Pagamento> findByInscricao(Inscricao inscricao);
}
