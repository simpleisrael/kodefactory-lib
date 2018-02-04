package com.kodefactory.tech.lib.security.dto;

import lombok.Data;

@Data
public class PasswordDTO {
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
