package com.qjrun.qjrun.util;

import com.qjrun.qjrun.enums.PerfilAcesso;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthUtil {

    public static void exigirAdmin(String perfilHeader) {
        try {
            PerfilAcesso perfil = PerfilAcesso.valueOf(perfilHeader);

            if (perfil != PerfilAcesso.ROLE_ADMIN) {
                // se não é o admin que está tentando criar um evento, lança um erro e trava a execução
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: apenas o administrador tem permissão.");
            }
        } catch (IllegalArgumentException e) {
            // se o perfil fornecido não for um dos listados no enum
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro: o perfil fornecido é inválido.");
        }
    }
}
