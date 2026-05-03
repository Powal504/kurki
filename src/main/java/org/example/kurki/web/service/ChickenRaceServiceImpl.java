package org.example.kurki.web.service;

import lombok.RequiredArgsConstructor;
import org.example.kurki.web.api.dto.ChickenRaceDto;
import org.example.kurki.web.api.mapper.ChickenRaceMapper;
import org.example.kurki.web.model.ChickenRace;
import org.example.kurki.web.repository.ChickenRaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChickenRaceServiceImpl implements ChickenRaceService {

    private final ChickenRaceRepository repository;
    private final ChickenRaceMapper mapper;

    @Override
    public List<ChickenRaceDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChickenRaceDto getOne(Long id) {
        return mapper.toDto(getRace(id));
    }

    @Override
    public ChickenRaceDto create(ChickenRaceDto dto) {
        ChickenRace race = new ChickenRace();
        race.setRace(dto.getRace());
        race.setDescription(dto.getDescription());

        return mapper.toDto(repository.save(race));
    }

    @Override
    public ChickenRaceDto update(Long id, ChickenRaceDto dto) {
        ChickenRace race = getRace(id);

        race.setRace(dto.getRace());
        race.setDescription(dto.getDescription());

        return mapper.toDto(repository.save(race));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private ChickenRace getRace(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Race not found"));
    }
}
