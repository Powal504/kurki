package org.example.kurki.web.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChickenRaceDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String race;
    private String description;
}
