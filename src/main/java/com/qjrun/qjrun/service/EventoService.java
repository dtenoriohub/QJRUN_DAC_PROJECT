package com.qjrun.qjrun.service;

import com.qjrun.qjrun.dto.EventoDTO;
import com.qjrun.qjrun.entity.Administrador;
import com.qjrun.qjrun.entity.Evento;
import com.qjrun.qjrun.repository.AdministradorRepository;
import com.qjrun.qjrun.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository repository;
    private final AdministradorRepository administradorRepository;

    public List<Evento> listar() {
        return repository.findByAtivoTrue();
    }

    @Transactional
    public Evento salvar(EventoDTO dto) {
        // 1. Recupera o email do administrador logado através do token JWT
        String emailAdmin = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Busca o administrador no banco
        Administrador admin = administradorRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado para este token."));

        // 3. Constrói o evento vinculado ao administrador
        Evento evento = Evento.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .local(dto.getLocal())
                .data(dto.getData())
                .horario(dto.getHorario())
                .vagas(dto.getVagas())
                .valor(dto.getValor())
                .ativo(true)
                .administrador(admin) // 🔑 Vínculo obrigatório definido na Entidade
                .build();

        return repository.save(evento);
    }

    @Transactional
    public Evento atualizar(Long id, EventoDTO dto) {
        Evento evento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + id));

        evento.setNome(dto.getNome());
        evento.setDescricao(dto.getDescricao());
        evento.setLocal(dto.getLocal());
        evento.setData(dto.getData());
        evento.setHorario(dto.getHorario());
        evento.setVagas(dto.getVagas());
        evento.setValor(dto.getValor());

        return repository.save(evento);
    }

    @Transactional
    public void deletar(Long id) {
        Evento evento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado."));

        // 🛡️ Soft Delete: Marca como inativo em vez de deletar
        evento.setAtivo(false);
        repository.save(evento);
    }
    // Dentro da classe EventoService.java

    public Evento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + id));
    }
}