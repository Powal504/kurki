package org.example.kurki.security.service;

import lombok.RequiredArgsConstructor;
import org.example.kurki.security.api.dto.UserResponseDto;
import org.example.kurki.security.api.mapper.UserMapper;
import org.example.kurki.security.model.User;
import org.example.kurki.security.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponseDto getCurrentUser() {
        User user = getAuthenticatedUser();
        return userMapper.toResponseDto(user);
    }

    public List<UserResponseDto> allUsers() {

        List<UserResponseDto> users = new ArrayList<>();

        userRepository.findAll()
                .forEach(user ->
                        users.add(userMapper.toResponseDto(user)));

        return users;
    }

    public void deleteOwnAccount() {
        User user = getAuthenticatedUser();

        userRepository.delete(user);
    }

    public void banUser(Long id) {

        User user = getUser(id);

        if ("ROLE_ADMIN".equals(user.getRole())
                || "ROLE_MODERATOR".equals(user.getRole())) {

            throw new RuntimeException(
                    "Nie można zbanować użytkownika z takimi uprawnieniami");
        }

        user.setBanned(true);

        userRepository.save(user);
    }

    public void deleteUserByAdmin(Long id) {

        User user = getUser(id);

        if ("ROLE_ADMIN".equals(user.getRole())) {
            throw new RuntimeException(
                    "Nie można usunąć administratora");
        }

        userRepository.delete(user);
    }

    private User getUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    private User getAuthenticatedUser() {

        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}