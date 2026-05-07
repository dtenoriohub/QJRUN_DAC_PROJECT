package com.qjrun.qjrun.service;

import com.qjrun.qjrun.entity.Administrador;
import com.qjrun.qjrun.repository.AdministradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    // CREATE
    @Transactional
    public Administrador save(Administrador administrador) {
        administrador.setId(null); // impede o envio de um ID prevenindo a atualização de um registro que já existe
        administrador.setAtivo(true);
        return administradorRepository.save(administrador);
    }

    // READ
    public List<Administrador> findAll() {
        return administradorRepository.findAllByAtivoTrue();
    }

    // READ
    public Administrador findById(long id) {
        return administradorRepository.findById(id).orElseThrow(() -> new RuntimeException("Administrador não encontrado!"));
    }

    // UPDATE
    public Administrador update(Long id, Administrador administradorAtualizado) {
        Administrador administradorExistente =  findById(id);

        administradorExistente.setNome(administradorAtualizado.getNome());
        administradorExistente.setTelefone(administradorAtualizado.getTelefone());

        validarEAtualizarEmail(administradorAtualizado, administradorExistente);

        return administradorRepository.save(administradorExistente);
    }

    // DELETE
    public void inativar(Long id) {
        Administrador administrador = findById(id);
        administrador.setAtivo(false);
        administradorRepository.save(administrador);
    }

    public void validarEAtualizarEmail(Administrador administradorAtualizado, Administrador administradorExistente) {
        String novoEmail = administradorAtualizado.getEmail();

        // Se não tiver nenhuma alteração real sai do método
        if(novoEmail == null || novoEmail.isBlank() || novoEmail.equals(administradorExistente.getEmail())) {
            return;
        }

        if(administradorRepository.findByEmail(novoEmail).isPresent()) {
            throw new RuntimeException("Ops! O e-mail " + novoEmail + " já está sendo usado!");
        }

        administradorExistente.setEmail(novoEmail);
    }
}