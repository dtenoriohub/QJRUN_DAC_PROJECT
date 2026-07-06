package com.qjrun.qjrun.service;

import com.qjrun.qjrun.dto.aluno.AlunoResponseDTO;
import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Plano;
import com.qjrun.qjrun.repository.AlunoRepository;
import com.qjrun.qjrun.repository.PlanoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;
    private final AlunoRepository alunoRepository;

    // CREATE
    @Transactional
    public Plano save(Plano plano) {
        return planoRepository.save(plano);
    }

    // READ
    public List<Plano> findAll() {
        return planoRepository.findAllByAtivoTrue();
    }

    // READ
    public Plano findById(Long id) {
        return planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Esse plano não existe!"));
    }

    // UPDATE
    @Transactional
    public Plano update(Long id, Plano dadosAtualizados) {
        Plano planoAntigo = findById(id);
        planoAntigo.setAtivo(false);
        planoRepository.save(planoAntigo);

        Plano novoPlano = new Plano();

        BeanUtils.copyProperties(dadosAtualizados, novoPlano, "id", "ativo");

        novoPlano.setAtivo(true);

        return planoRepository.save(novoPlano);
    }

    // DELETE
    @Transactional
    public void desativar(Long id) {
        Plano plano = findById(id);
        plano.setAtivo(false);
        planoRepository.save(plano);
    }

    // Listar alunos por plano
    public List<AlunoResponseDTO> listarAlunosPorPlano(Long id) {
        // Garante que o plano existe (se não existir, já lança a exceção do findById)
        Plano planoSelecionado = findById(id);

        // Busca os alunos associados a este plano
        List<Aluno> alunos = alunoRepository.findAllByPlanoId(id);

        // Mapeamento manual usando o Builder
        return alunos.stream()
                .map(aluno -> AlunoResponseDTO.builder()
                        .id(aluno.getId())
                        .nome(aluno.getNome())
                        .matricula(aluno.getMatricula())
                        .cpf(aluno.getCpf())
                        .email(aluno.getEmail())
                        .telefone(aluno.getTelefone())
                        .dataNascimento(aluno.getDataNascimento())
                        .ativo(aluno.getAtivo()) // Pode ser isAtivo() dependendo de como o Lombok gerou na classe Usuario
                        .plano(planoSelecionado.getTipo()) // Garantido de existir
                        .turma(aluno.getTurma() != null ? aluno.getTurma().getNome() : "Sem Turma") // Protegido contra NullPointer
                        .build())
                .collect(Collectors.toList());
    }
}
