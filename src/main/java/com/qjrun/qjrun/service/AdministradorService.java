package com.qjrun.qjrun.service;

import com.qjrun.qjrun.dto.auth.CadastroRequestDTO;
import com.qjrun.qjrun.entity.Administrador;
import com.qjrun.qjrun.enums.PerfilAcesso;
import com.qjrun.qjrun.repository.AdministradorRepository;
import com.qjrun.qjrun.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // CREATE
    @Transactional
    public Administrador save(CadastroRequestDTO dto) {

        validarEmailUnico(dto.getEmail());
        validarCpfUnico(dto.getCpf());

        Administrador administrador = Administrador.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .perfilAcesso(PerfilAcesso.ROLE_ADMIN)
                .ativo(true)
                .build();

        return administradorRepository.save(administrador);
    }

    // READ
    public List<Administrador> findAll() {
        return administradorRepository.findAllByAtivoTrue();
    }

    // READ
    public Administrador findById(Long id) {
        return administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado!"));
    }

    // UPDATE
    @Transactional
    public Administrador update(Long id, Administrador administradorAtualizado) {

        Administrador administradorExistente = findById(id);

        administradorExistente.setNome(administradorAtualizado.getNome());
        administradorExistente.setTelefone(administradorAtualizado.getTelefone());

        validarEAtualizarEmail(administradorAtualizado, administradorExistente);

        return administradorRepository.save(administradorExistente);
    }

    // DELETE (soft delete)
    @Transactional
    public void inativar(Long id) {

        Administrador administrador = findById(id);
        administrador.setAtivo(false);

        administradorRepository.save(administrador);
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================

    private void validarEAtualizarEmail(Administrador administradorAtualizado,
                                        Administrador administradorExistente) {

        String novoEmail = administradorAtualizado.getEmail();

        if (novoEmail == null ||
                novoEmail.isBlank() ||
                novoEmail.equals(administradorExistente.getEmail())) {
            return;
        }

        validarEmailUnico(novoEmail);

        administradorExistente.setEmail(novoEmail);
    }

    private void validarEmailUnico(String email) {

        if (email != null &&
                !email.isBlank() &&
                usuarioRepository.existsByEmail(email)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ops! O e-mail " + email + " já está cadastrado no sistema!"
            );
        }
    }

    private void validarCpfUnico(String cpf) {

        if (cpf != null &&
                !cpf.isBlank() &&
                usuarioRepository.existsByCpf(cpf)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ops! O CPF " + cpf + " já está cadastrado no sistema!"
            );
        }
    }
}