package org.example.kurki.security.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class RegisterUserDto {

    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).+$",
            message = "Password must contain at least one uppercase letter and one special character"
    )
    private String password;

    private LocalDate dateOfBirth;

    @Pattern(
            regexp = "^(\\+\\d{11}|\\d{9})$",
            message = "Phone number must be 9 digits or 11 digits with country code (e.g. +48123456789)"
    )
    private String phoneNumber;
}
