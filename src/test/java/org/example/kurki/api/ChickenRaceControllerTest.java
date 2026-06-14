package org.example.kurki.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kurki.security.configuration.ApplicationConfiguration;
import org.example.kurki.security.configuration.JwtAuthenticationFilter;
import org.example.kurki.security.configuration.SecurityConfiguration;
import org.example.kurki.security.repository.UserRepository;
import org.example.kurki.security.service.JwtService;
import org.example.kurki.web.api.controller.ChickenRaceController;
import org.example.kurki.web.api.dto.ChickenRaceDto;
import org.example.kurki.web.service.ChickenRaceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChickenRaceController.class)
@Import({
        SecurityConfiguration.class,
        ApplicationConfiguration.class,
        JwtAuthenticationFilter.class
})
class ChickenRaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChickenRaceService service;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void getAll_shouldReturnList() throws Exception {
        ChickenRaceDto dto = new ChickenRaceDto();
        dto.setId(1L);
        dto.setRace("maka");
        dto.setDescription("Fluffy chicken");

        when(service.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/races"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].race").value("maka"))
                .andExpect(jsonPath("$[0].description").value("Fluffy chicken"));
    }

    @Test
    @WithMockUser
    void getOne_shouldReturnRace() throws Exception {
        ChickenRaceDto dto = new ChickenRaceDto();
        dto.setId(1L);
        dto.setRace("Silkie");

        when(service.getOne(1L)).thenReturn(dto);

        mockMvc.perform(get("/races/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.race").value("Silkie"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void create_shouldCreateRace() throws Exception {
        ChickenRaceDto dto = new ChickenRaceDto();
        dto.setRace("Leghorn");
        dto.setDescription("Fast chicken");

        when(service.create(any())).thenReturn(dto);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.race").value("Leghorn"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/races/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void delete_shouldReturnForbiddenForUser() throws Exception {

        mockMvc.perform(delete("/races/1"))
                .andExpect(status().isForbidden());
    }
}