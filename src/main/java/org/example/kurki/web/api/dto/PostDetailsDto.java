package org.example.kurki.web.api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class PostDetailsDto {
    private String title;
    private String text;
    private LocalDate creationDate;
    private Boolean isBanned;

    private UserDto user;
    private Set<ChickenRaceDto> races;
}