package org.example.kurki.security.api.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String email;
    private String verificationCode;
    private String newPassword;
}
