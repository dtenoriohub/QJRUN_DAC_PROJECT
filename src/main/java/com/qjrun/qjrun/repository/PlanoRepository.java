package com.qjrun.qjrun.repository;

import com.qjrun.qjrun.entity.Plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {

    List<Plano> findAllByAtivoTrue();

    Optional<Plano> findByTipo(String tipo);}