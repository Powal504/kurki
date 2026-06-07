package org.example.kurki.security.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.kurki.security.api.dto.UserResponseDto;
import org.example.kurki.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize(
            "hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public ResponseEntity<UserResponseDto> authenticatedUser() {

        return ResponseEntity.ok(
                userService.getCurrentUser());
    }

    @GetMapping
    @PreAuthorize(
            "hasAnyRole('MODERATOR', 'ADMIN')")
    public ResponseEntity<List<UserResponseDto>> allUsers() {

        return ResponseEntity.ok(
                userService.allUsers());
    }

    @DeleteMapping("/me")
    @PreAuthorize(
            "hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public ResponseEntity<Void> deleteOwnAccount() {

        userService.deleteOwnAccount();

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ban")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> banUser(
            @PathVariable Long id) {

        userService.banUser(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUserByAdmin(id);

        return ResponseEntity.noContent().build();
    }
}