package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.dto.aluno.AlunoResponseDTO;
import com.qjrun.qjrun.dto.turma.TurmaRequestDTO;
import com.qjrun.qjrun.dto.turma.TurmaResponseDTO;
import com.qjrun.qjrun.service.TurmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turmas")
@RequiredArgsConstructor
public class TurmaController {

    private final TurmaService turmaService;

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TurmaResponseDTO create(@RequestBody TurmaRequestDTO dto) {
        return turmaService.save(dto);
    }

    // READ: Buscar alunos da turma
    @GetMapping("/{id}/alunos")
    public List<AlunoResponseDTO> listarAlunosDaTurma(@PathVariable Long id) {
        return turmaService.listarAlunosPorTurma(id);
    }

    // READ (Listagem)
    @GetMapping
    public List<TurmaResponseDTO> findAll() {
        return turmaService.findAllDTO();
    }

    // UPDATE
    @PutMapping("/{id}")
    public TurmaResponseDTO update(@PathVariable Long id, @RequestBody TurmaRequestDTO dto) {
        return turmaService.update(id, dto);
    }

    // DELETE (Soft Delete)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        turmaService.desativar(id);
    }

    // --- NOVO RECURSO: VINCULAR ALUNO À TURMA ---

    @PostMapping("/{turmaId}/alunos/{alunoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vincularAluno(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        turmaService.adicionarAlunoNaTurma(turmaId, alunoId);
    }
    @DeleteMapping("/{turmaId}/alunos/{alunoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerAluno(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        turmaService.removerAlunoDaTurma(turmaId, alunoId);
    }
}