package com.qjrun.qjrun.dto.auth;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String email;

    private String senha;

}