package com.qjrun.qjrun.service;

import com.qjrun.qjrun.entity.Administrador;
import com.qjrun.qjrun.enums.PerfilAcesso;
import com.qjrun.qjrun.repository.AdministradorRepository;
import com.qjrun.qjrun.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final UsuarioRepository usuarioRepository;

    // CREATE
    @Transactional
    public Administrador save(Administrador administrador) {
        administrador.setId(null); // impede o envio de um ID prevenindo a atualização de um registro que já existe
        administrador.setAtivo(true);

        administrador.setPerfilAcesso(PerfilAcesso.ROLE_ADMIN);

        validarEmailUnico(administrador.getEmail());

        return administradorRepository.save(administrador);
    }

    // READ
    public List<Administrador> findAll() {
        return administradorRepository.findAllByAtivoTrue();
    }

    // READ
    public Administrador findById(Long id) {
        return administradorRepository.findById(id).orElseThrow(() -> new RuntimeException("Administrador não encontrado!"));
    }

    // UPDATE
    @Transactional
    public Administrador update(Long id, Administrador administradorAtualizado) {
        Administrador administradorExistente =  findById(id);

        administradorExistente.setNome(administradorAtualizado.getNome());
        administradorExistente.setTelefone(administradorAtualizado.getTelefone());

        validarEAtualizarEmail(administradorAtualizado, administradorExistente);

        return administradorRepository.save(administradorExistente);
    }

    // DELETE
    @Transactional
    public void inativar(Long id) {
        Administrador administrador = findById(id);
        administrador.setAtivo(false);
        administradorRepository.save(administrador);
    }

    // METODOS AUXILIARES
    private void validarEAtualizarEmail(Administrador administradorAtualizado, Administrador administradorExistente) {

        String novoEmail = administradorAtualizado.getEmail();

        // Se não tiver nenhuma alteração real sai do método
        if(novoEmail == null || novoEmail.isBlank() || novoEmail.equals(administradorExistente.getEmail())) {
            return;
        }

        if(usuarioRepository.findByEmail(novoEmail).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ops! O e-mail " + novoEmail + " já está sendo usado!");
        }

        administradorExistente.setEmail(novoEmail);
    }

    private void validarEmailUnico(String email) {

        if (email != null && !email.isBlank() && usuarioRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ops! O e-mail " + email + " já está cadastrado no sistema!");
        }
    }
}