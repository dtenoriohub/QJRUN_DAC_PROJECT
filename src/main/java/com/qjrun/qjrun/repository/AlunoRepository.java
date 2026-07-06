package com.qjrun.qjrun.repository;

import com.qjrun.qjrun.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    int countByTurmaIdAndAtivoTrue(Long turmaId);

    List<Aluno> findAllByAtivoTrue();

    Optional<Aluno> findByEmail(String email);

    Optional<Aluno> findByCpf(String cpf);

    // Dentro de AlunoRepository
    List<Aluno> findTop5ByOrderByNomeAsc(); // Ou por data de cadastro se você tiver esse campo

    List<Aluno> findAllByPlanoId(Long planoId);
}