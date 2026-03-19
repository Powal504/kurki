package org.example.kurki.security.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class RegisterUserDto {
    private String username;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String phoneNumber;
}
