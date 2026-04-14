package com.qjrun.qjrun.repository;

import com.qjrun.qjrun.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findAllByAtivoTrue();

}
