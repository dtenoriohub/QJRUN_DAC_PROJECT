package com.qjrun.qjrun.controller;

import com.qjrun.qjrun.dto.auth.CadastroRequestDTO;
import com.qjrun.qjrun.dto.auth.CadastroResponseDTO;
import com.qjrun.qjrun.dto.auth.LoginRequestDTO;
import com.qjrun.qjrun.dto.auth.LoginResponseDTO;
import com.qjrun.qjrun.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO dto
    ) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<CadastroResponseDTO> register(
            @RequestBody @Valid CadastroRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.cadastrar(dto));
    }
}