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

    public List<Evento> listar() {
        return eventoRepository.findAllByAtivoTrue();
    }

    public Evento criar(Evento evento) {
        return eventoRepository.save(evento);
    }

    public Evento buscar(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado."));
    }

    public void deletar(Long id) {
        Evento evento = buscar(id);
        evento.setAtivo(false);
        eventoRepository.save(evento);
    }
    public Evento atualizar(Long id, Evento novoEvento) {
        Evento evento = buscar(id);

        evento.setNome(novoEvento.getNome());
        evento.setDescricao(novoEvento.getDescricao());
        evento.setLocal(novoEvento.getLocal());
        evento.setData(novoEvento.getData());
        evento.setHorario(novoEvento.getHorario());
        evento.setVagas(novoEvento.getVagas());

        return eventoRepository.save(evento);
    }
}