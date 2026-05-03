package org.example.kurki.web.api.mapper;

import org.example.kurki.web.api.dto.ChickenRaceDto;
import org.example.kurki.web.model.ChickenRace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChickenRaceMapper {

    ChickenRaceDto toDto(ChickenRace chickenRace);

}
