package org.example.kurki.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kurki.security.configuration.ApplicationConfiguration;
import org.example.kurki.security.configuration.JwtAuthenticationFilter;
import org.example.kurki.security.configuration.SecurityConfiguration;
import org.example.kurki.security.repository.UserRepository;
import org.example.kurki.security.service.JwtService;
import org.example.kurki.web.api.controller.PostController; // Upewnij się, że to poprawny import
import org.example.kurki.web.api.dto.PostDetailsDto;
import org.example.kurki.web.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@Import({
        SecurityConfiguration.class,
        ApplicationConfiguration.class,
        JwtAuthenticationFilter.class
})
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void getAll_shouldReturnPageOfPosts() throws Exception {
        PostDetailsDto dto = new PostDetailsDto();
        dto.setTitle("Test Post");
        Page<PostDetailsDto> page = new PageImpl<>(List.of(dto));

        when(postService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/posts?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Post"));
    }

    @Test
    @WithMockUser
    void getOne_shouldReturnPost() throws Exception {
        PostDetailsDto dto = new PostDetailsDto();
        dto.setTitle("Single Post");

        when(postService.getOne(1)).thenReturn(dto);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Single Post"));
    }

    @Test
    @WithMockUser
    void create_shouldCreatePost() throws Exception {
        PostDetailsDto dto = new PostDetailsDto();
        dto.setTitle("New Post");
        dto.setText("Content");

        when(postService.create(any())).thenReturn(dto);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Post"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void ban_shouldReturnOkForModerator() throws Exception {
        // Założenie: endpoint to PATCH /posts/{id}/ban i wymaga roli MODERATOR
        mockMvc.perform(patch("/posts/1/ban"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void ban_shouldReturnForbiddenForUser() throws Exception {
        mockMvc.perform(patch("/posts/1/ban"))
                .andExpect(status().isForbidden()); // Oczekujemy kodu 403 z GlobalExceptionHandlera
    }
}