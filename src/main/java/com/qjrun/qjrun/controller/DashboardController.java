package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.dto.dashboard.DashboardDTO;
import com.qjrun.qjrun.repository.AlunoRepository;
import com.qjrun.qjrun.repository.PlanoRepository;
import com.qjrun.qjrun.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor // O Lombok cuida da injeção aqui
public class DashboardController {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final PlanoRepository planoRepository; // Certifique-se de que este campo existe

    @GetMapping
    public DashboardDTO getDashboardData() {
        return DashboardDTO.builder()
                .totalAlunos(alunoRepository.count()) // Se quiser apenas ativos, mude para countByAtivoTrue()
                .totalTurmas(turmaRepository.countByAtivoTrue())
                .totalPlanos(planoRepository.countByAtivoTrue()) // Usando o método que acabamos de criar
                .ultimosCadastros(alunoRepository.findTop5ByOrderByNomeAsc()
                        .stream().map(a -> a.getNome()).toList())
                .build();
    }
}