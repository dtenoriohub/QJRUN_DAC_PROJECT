package com.qjrun.qjrun.service;

import com.qjrun.qjrun.entity.Administrador;
import com.qjrun.qjrun.entity.Evento;
import com.qjrun.qjrun.repository.AdministradorRepository;
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
    private final AdministradorRepository administradorRepository;

    // CREATE
    @Transactional
    public Evento save(Evento evento, Long administradorId) {
        // procura o admin que está fazendo a requisição
        Administrador administrador = administradorRepository.findById(administradorId).orElseThrow(() -> new RuntimeException("Administrador não encontrado."));

        evento.setId(null); // impede atualização acidental de um evento que já existe
        evento.setAtivo(true); // garante que novos eventos "nasçam" ativos

        // vincula o administrador ao evento
        evento.setAdministrador(administrador);

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
