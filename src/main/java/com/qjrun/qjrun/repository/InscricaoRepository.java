package com.qjrun.qjrun.repository;

import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Evento;
import com.qjrun.qjrun.entity.Inscricao;
import com.qjrun.qjrun.enums.StatusInscricao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    // impede que um aluno se inscreva duas vezes na mesma corrida
    boolean existsByAlunoAndEventoAndAtivoTrue(Aluno aluno, Evento evento);

    // conta quantos alunos garantiram vagas
    long countByEventoAndAtivoTrue(Evento evento);

    // busca para cancelamento
    Optional<Inscricao> findByAlunoAndEventoAndAtivoTrue(Aluno aluno, Evento evento);

    // histórico de inscrições do aluno
    List<Inscricao> findAllByAlunoAndAtivoTrue(Aluno aluno);

    List<Inscricao> findByStatusAndAtivoTrue(StatusInscricao status);

    // Para o admin: Paginação com Pendentes no topo!
    // Se for PENDENTE, peso 0 (topo). Senão, peso 1. O empate é resolvido pelo ID mais recente.
    @Query("SELECT i FROM Inscricao i WHERE i.ativo = true ORDER BY CASE WHEN i.status = 'PENDENTE' THEN 0 ELSE 1 END, i.id DESC")
    Page<Inscricao> findAllAtivoTruePriorizandoPendentes(Pageable pageable);

}