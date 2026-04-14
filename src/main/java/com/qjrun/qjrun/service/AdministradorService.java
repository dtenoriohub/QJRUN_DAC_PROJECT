package com.qjrun.qjrun.service;

import com.qjrun.qjrun.entity.Administrador;
import com.qjrun.qjrun.repository.AdministradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    public Administrador save(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    public List<Administrador> findAll() {
        return administradorRepository.findAll();
    }

    public Administrador findById(long id) {
        return administradorRepository.findById(id).orElseThrow(() -> new RuntimeException("Administrador não encontrado!"));
    }

    public Administrador update(Long id, Administrador administradorAtualizado) {
        Administrador administrador =  findById(id);

        administrador.setNome(administradorAtualizado.getNome());
        administrador.setTelefone(administradorAtualizado.getTelefone());

        String novoEmail = administradorAtualizado.getEmail();
        String emailAtual =  administrador.getEmail();

        if (novoEmail != null && !novoEmail.equals(emailAtual)) {
            Optional<Administrador> donoDoEmail = administradorRepository.findByEmail(novoEmail);

            if (donoDoEmail.isPresent()) {
                throw new RuntimeException("Ops! O e-mail " + novoEmail + " já está sendo usado!");
            }

            administrador.setEmail(novoEmail);
        }

        return administradorRepository.save(administrador);
    }

    public void inativar(Long id) {
        Administrador administrador = findById(id);
        administrador.setAtivo(false);
        administradorRepository.save(administrador);
    }
}
