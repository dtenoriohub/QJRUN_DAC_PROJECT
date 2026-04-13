package com.qjrun.qjrun.repository;

import com.qjrun.qjrun.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    List<Aluno> findAllByAtivoTrue();
    Optional<Aluno> findByEmail(String email);
}



