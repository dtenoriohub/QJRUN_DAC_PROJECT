package com.qjrun.qjrun.repository;

import com.qjrun.qjrun.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    List<Turma> findAllByAtivoTrue();
    long countByAtivoTrue(); // <--- Adicione esta linha
}