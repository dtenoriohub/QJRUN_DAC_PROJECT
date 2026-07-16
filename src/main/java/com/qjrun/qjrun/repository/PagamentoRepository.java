package com.qjrun.qjrun.repository;

import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Inscricao;
import com.qjrun.qjrun.entity.Pagamento;
import com.qjrun.qjrun.enums.StatusPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByAlunoId(Long alunoId);

    List<Pagamento> findByStatus(StatusPagamento status);

    boolean existsByAlunoAndStatus(Aluno aluno, StatusPagamento status);

    List<Pagamento> findByInscricao(Inscricao inscricao);

    // Atrasados (0) no topo absoluto, Pendentes (1) logo abaixo, e o resto (2) depois.
    @Query("SELECT p FROM Pagamento p ORDER BY CASE WHEN p.status = 'ATRASADO' THEN 0 WHEN p.status = 'PENDENTE' THEN 1 ELSE 2 END, p.id DESC")
    Page<Pagamento> findAllPriorizandoAtrasadosEPendentes(Pageable pageable);
}