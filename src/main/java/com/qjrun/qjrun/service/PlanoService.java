package com.qjrun.qjrun.service;

import com.qjrun.qjrun.entity.Plano;
import com.qjrun.qjrun.repository.PlanoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;

    // CREATE
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
    public void desativar(Long id) {
        Plano plano = findById(id);
        plano.setAtivo(false);
        planoRepository.save(plano);
    }
}
