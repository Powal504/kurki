package org.example.kurki.web.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class PostDetailsDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String title;
    private String text;
    private LocalDate creationDate;
    private Boolean isBanned;

    private UserDto user;
    private Set<ChickenRaceDto> races;
}