package org.example.kurki.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kurki.security.api.controller.AuthenticationController; // Upewnij się, że import jest poprawny
import org.example.kurki.security.api.dto.LoginUserDto;
import org.example.kurki.security.api.dto.RegisterUserDto;
import org.example.kurki.security.api.dto.VerifyUserDto;
import org.example.kurki.security.configuration.ApplicationConfiguration;
import org.example.kurki.security.configuration.JwtAuthenticationFilter;
import org.example.kurki.security.configuration.SecurityConfiguration;
import org.example.kurki.security.model.User;
import org.example.kurki.security.repository.UserRepository;
import org.example.kurki.security.service.AuthenticationService;
import org.example.kurki.security.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@Import({
        SecurityConfiguration.class,
        ApplicationConfiguration.class,
        JwtAuthenticationFilter.class
})
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void signup_shouldReturnOk() throws Exception {
        RegisterUserDto dto = new RegisterUserDto();
        dto.setUsername("testuser");
        dto.setEmail("test@test.com");
        dto.setPassword("Password123!");

        User mockedUser = new User();
        mockedUser.setEmail("test@test.com");

        when(authenticationService.signup(any(RegisterUserDto.class))).thenReturn(mockedUser);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void authenticate_shouldReturnToken() throws Exception {
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("test@test.com");
        loginDto.setPassword("Password123!");

        User authenticatedUser = new User();
        authenticatedUser.setEmail("test@test.com");

        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(authenticatedUser);
        when(jwtService.generateToken(authenticatedUser)).thenReturn("mocked.jwt.token");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked.jwt.token"));
    }

    @Test
    void verify_shouldReturnOk() throws Exception {
        VerifyUserDto dto = new VerifyUserDto();
        dto.setEmail("test@test.com");
        dto.setVerificationCode("123456");

        doNothing().when(authenticationService).verifyUser(any(VerifyUserDto.class));

        mockMvc.perform(post("/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}