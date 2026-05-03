package org.example.kurki.web.service;

import org.example.kurki.web.api.dto.ChickenRaceDto;

import java.util.List;

public interface ChickenRaceService {

    List<ChickenRaceDto> getAll();

    ChickenRaceDto getOne(Long id);

    ChickenRaceDto create(ChickenRaceDto dto);

    ChickenRaceDto update(Long id, ChickenRaceDto dto);

    void delete(Long id);
}