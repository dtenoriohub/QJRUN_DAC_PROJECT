package com.qjrun.qjrun.service;

import com.qjrun.qjrun.dto.auth.*;
import com.qjrun.qjrun.entity.Aluno;
import com.qjrun.qjrun.entity.Plano;
import com.qjrun.qjrun.entity.Usuario;
import com.qjrun.qjrun.enums.PerfilAcesso;
import com.qjrun.qjrun.mapper.AuthMapper;
import com.qjrun.qjrun.repository.AlunoRepository;
import com.qjrun.qjrun.repository.PlanoRepository;
import com.qjrun.qjrun.repository.UsuarioRepository;
import com.qjrun.qjrun.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PlanoRepository planoRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;


    private String gerarMatricula() {
        return "MAT" + System.currentTimeMillis();
    }
    /**
     * LOGIN
     */
    public LoginResponseDTO login(LoginRequestDTO dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getSenha()
                )
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String token = jwtService.generateToken(usuario);

        return AuthMapper.toLoginResponse(usuario, token);
    }

    /**
     * CADASTRO
     */
    public CadastroResponseDTO cadastrar(CadastroRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado.");
        }


        Plano planoPadrao = planoRepository.findByTipo("FREE")
                .orElseThrow(() ->
                        new RuntimeException("Plano FREE não encontrado."));

        Aluno aluno = new Aluno();

        aluno.setNome(dto.getNome());
        aluno.setCpf(dto.getCpf());
        aluno.setEmail(dto.getEmail());
        aluno.setSenha(passwordEncoder.encode(dto.getSenha()));
        aluno.setTelefone(dto.getTelefone());
        aluno.setDataNascimento(dto.getDataNascimento());

        aluno.setPerfilAcesso(PerfilAcesso.ROLE_ALUNO);
        aluno.setPlano(planoPadrao);
        aluno.setAtivo(true);


        aluno.setMatricula(gerarMatricula());

        alunoRepository.save(aluno);

        return new CadastroResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                "Usuário cadastrado com sucesso."
        );
    }
}