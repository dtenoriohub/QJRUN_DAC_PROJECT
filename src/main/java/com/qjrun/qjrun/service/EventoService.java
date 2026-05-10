package com.qjrun.qjrun.service;

import com.qjrun.qjrun.entity.Evento;
import lombok.RequiredArgsConstructor;
import com.qjrun.qjrun.repository.EventoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    // CREATE
    @Transactional
    public Evento save(Evento evento) {
        evento.setId(null); // impede atualização acidental de um evento que já existe
        evento.setAtivo(true); // garante que novos eventos "nasçam" ativos
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
    @Transactional
    public Evento update(Long id, Evento eventoAtualizado) {
        Evento eventoExistente = findById(id);

        atualizarDadosBase(eventoAtualizado, eventoExistente);

        return eventoRepository.save(eventoExistente);
    }

    // DELETE
    @Transactional
    public void desativar(Long id) {
        Evento evento = findById(id);
        evento.setAtivo(false);
        eventoRepository.save(evento);
    }

    // MÉTODOS AUXILIARES
    private void atualizarDadosBase(Evento eventoAtualizado, Evento eventoExistente) {

        // Copia tudo do JSON para o banco, menos o ID e o status
        BeanUtils.copyProperties(eventoAtualizado, eventoExistente, "id", "ativo");
    }
}
