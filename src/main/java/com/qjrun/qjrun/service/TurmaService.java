package com.qjrun.qjrun.service;

import com.qjrun.qjrun.entity.Turma;
import com.qjrun.qjrun.repository.AdministradorRepository;
import com.qjrun.qjrun.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository turmaRepository;

    // CREATE
    public Turma save(Turma turma) {
        return turmaRepository.save(turma);
    }

    // READ
    public List<Turma> findAll() {
        return turmaRepository.findAllByAtivoTrue();
    }

    // READ
    public Turma findById(Long id) {
        return turmaRepository.findById(id).orElseThrow(() -> new RuntimeException("Essa turma não existe!"));
    }

    // UPDATE
    public Turma update(Long id, Turma dadosAtualizados) {
        Turma turma = findById(id);

        BeanUtils.copyProperties(dadosAtualizados, turma, "id", "ativo");

        return turmaRepository.save(turma);
    }

    // DELETE
    public void desativar(Long id) {
        Turma turma = findById(id);
        turma.setAtivo(false);
        turmaRepository.save(turma);
    }

}
