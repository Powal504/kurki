package org.example.kurki.ai.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChatRequestDto {
    @NotBlank
    @Size(max = 1000)
    private String message;
}
