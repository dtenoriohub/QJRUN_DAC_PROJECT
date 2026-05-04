package com.qjrun.qjrun.service;

import com.qjrun.qjrun.entity.Evento;
import lombok.RequiredArgsConstructor;
import com.qjrun.qjrun.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    // CREATE
    public Evento save(Evento evento) {
        return eventoRepository.save(evento);
    }

    // READ
    public List<Evento> findAll() {
        return eventoRepository.findAllByAtivoTrue();
    }

    // READ
    public Evento findById(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado."));
    }

    // UPDATE
    public Evento update(Long id, Evento novoEvento) {
        Evento evento = findById(id);

        evento.setNome(novoEvento.getNome());
        evento.setDescricao(novoEvento.getDescricao());
        evento.setLocal(novoEvento.getLocal());
        evento.setData(novoEvento.getData());
        evento.setHorario(novoEvento.getHorario());
        evento.setVagas(novoEvento.getVagas());

        return eventoRepository.save(evento);
    }

    // DELETE
    public void desativar(Long id) {
        Evento evento = findById(id);
        evento.setAtivo(false);
        eventoRepository.save(evento);
    }
}
