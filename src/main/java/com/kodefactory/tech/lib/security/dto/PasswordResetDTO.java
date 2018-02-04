package com.kodefactory.tech.lib.security.dto;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String email;
    private String token;
}
