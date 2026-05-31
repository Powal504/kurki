package org.example.kurki.ai.api.controller;

import lombok.RequiredArgsConstructor;

import org.example.kurki.ai.api.dto.ChatRequestDto;
import org.example.kurki.ai.api.dto.ChatResponseDto;
import org.example.kurki.ai.service.ChatbotService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public ChatResponseDto chat(@RequestBody ChatRequestDto dto) {
        return new ChatResponseDto(chatbotService.ask(dto.getMessage()));
    }
}
