package com.qjrun.qjrun.service;

import com.qjrun.qjrun.dto.aluno.AlunoResponseDTO;
import com.qjrun.qjrun.dto.turma.TurmaRequestDTO;
import com.qjrun.qjrun.dto.turma.TurmaResponseDTO;
import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Turma;
import com.qjrun.qjrun.repository.AlunoRepository;
import com.qjrun.qjrun.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;

    // CREATE
    @Transactional
    public TurmaResponseDTO save(TurmaRequestDTO dto) {
        Turma turma = new Turma();
        BeanUtils.copyProperties(dto, turma);
        turma.setAtivo(true);

        Turma turmaSalva = turmaRepository.save(turma);
        return converterParaDTO(turmaSalva, 0);
    }

    // READ (Listagem)
    public List<TurmaResponseDTO> findAllDTO() {
        return turmaRepository.findAllByAtivoTrue().stream().map(turma -> {
            int qtdAlunos = alunoRepository.countByTurmaIdAndAtivoTrue(turma.getId());
            return converterParaDTO(turma, qtdAlunos);
        }).collect(Collectors.toList());
    }

    // READ (Uso interno)
    public Turma findById(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Essa turma não existe!"));
    }

    // UPDATE
    @Transactional
    public TurmaResponseDTO update(Long id, TurmaRequestDTO dadosAtualizados) {
        Turma turma = findById(id);
        BeanUtils.copyProperties(dadosAtualizados, turma, "id", "ativo");

        Turma turmaAtualizada = turmaRepository.save(turma);
        int qtdAlunos = alunoRepository.countByTurmaIdAndAtivoTrue(turma.getId());

        return converterParaDTO(turmaAtualizada, qtdAlunos);
    }

    // DELETE (Soft Delete)
    @Transactional
    public void desativar(Long id) {
        Turma turma = findById(id);
        turma.setAtivo(false);
        turmaRepository.save(turma);
    }

    // READ: Buscar alunos da turma
    public List<AlunoResponseDTO> listarAlunosPorTurma(Long turmaId) {
        Turma turma = findById(turmaId);

        // Mapeamento manual usando o Builder (assumindo que o DTO tenha @Builder)
        return turma.getAlunos().stream()
                .map(aluno -> AlunoResponseDTO.builder()
                        .id(aluno.getId())
                        .nome(aluno.getNome())
                        .email(aluno.getEmail())
                        // Adicione aqui os outros campos que existirem no seu AlunoResponseDTO
                        .build())
                .collect(Collectors.toList());
    }

    // VINCULAR ALUNO À TURMA
    @Transactional
    public void adicionarAlunoNaTurma(Long turmaId, Long alunoId) {
        Turma turma = findById(turmaId);
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));

        aluno.setTurma(turma);
        alunoRepository.save(aluno);
    }

    // CONVERSOR PRIVADO
    private TurmaResponseDTO converterParaDTO(Turma turma, int quantidadeAlunos) {
        return TurmaResponseDTO.builder()
                .id(turma.getId())
                .nome(turma.getNome())
                .nivelTurma(turma.getNivelTurma())
                .horarioInicio(turma.getHorarioInicio())
                .horarioTermino(turma.getHorarioTermino())
                .ativo(turma.getAtivo())
                .quantidadeAlunosAtuais(quantidadeAlunos)
                .build();
    }
    @Transactional
    public void removerAlunoDaTurma(Long turmaId, Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado."));

        // Verifica se o aluno realmente pertence à turma antes de remover
        if (aluno.getTurma() == null || !aluno.getTurma().getId().equals(turmaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este aluno não pertence a esta turma.");
        }

        aluno.setTurma(null); // Remove a associação
        alunoRepository.save(aluno);
    }
}