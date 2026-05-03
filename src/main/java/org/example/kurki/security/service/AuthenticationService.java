package org.example.kurki.security.service;

import lombok.RequiredArgsConstructor;

import org.example.kurki.security.api.dto.ForgotPasswordDTO;
import org.example.kurki.security.api.dto.LoginUserDto;
import org.example.kurki.security.api.dto.RegisterUserDto;
import org.example.kurki.security.api.dto.ResetPasswordDTO;
import org.example.kurki.security.api.dto.VerifyUserDto;
import org.example.kurki.security.api.mapper.UserMapper;
import org.example.kurki.security.exception.UserNotFoundException;
import org.example.kurki.security.model.User;
import org.example.kurki.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecureRandom secureRandom = new SecureRandom();
    private final EmailService emailService;
    private final UserMapper userMapper;



    public User signup(RegisterUserDto input){
        User user = userMapper.toEntity(input);
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setDateOfBirth(input.getDateOfBirth());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input){
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!user.isEnabled()){
            throw new RuntimeException("Account not verified");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return user;
    }

    public void verifyUser(VerifyUserDto input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.isEnabled()){
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);

        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forgotPassword(ForgotPasswordDTO input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setResetPasswordCode(generateVerificationCode());
        user.setResetPasswordExpiresAt(LocalDateTime.now().plusMinutes(15));

        sendResetPasswordEmail(user);
        userRepository.save(user);
    }

    public void resetPassword(ResetPasswordDTO input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getResetPasswordExpiresAt() == null ||
                user.getResetPasswordExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset code expired");
        }

        if (!user.getResetPasswordCode().equals(input.getVerificationCode())) {
            throw new RuntimeException("Invalid reset code");
        }

        user.setPassword(passwordEncoder.encode(input.getNewPassword()));
        user.setResetPasswordCode(null);
        user.setResetPasswordExpiresAt(null);

        userRepository.save(user);
    }

    private void sendResetPasswordEmail(User user) {
        String subject = "Reset Password Request";
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Reset your password</h2>"
                + "<p style=\"font-size: 16px;\">Use the code below to reset your password:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px;\">"
                + "<h3>Reset Code:</h3>"
                + "<p style=\"font-size: 20px; font-weight: bold; color: #007bff;\">"
                + user.getResetPasswordCode()
                + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        int code = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
