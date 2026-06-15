package org.example.kurki.web.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.kurki.web.api.dto.ChickenRaceDto;
import org.example.kurki.web.service.ChickenRaceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/races")
@RequiredArgsConstructor
public class ChickenRaceController {

    private final ChickenRaceService service;

    @GetMapping
    public List<ChickenRaceDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ChickenRaceDto getOne(@PathVariable Long id) {
        return service.getOne(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ChickenRaceDto create(@RequestBody ChickenRaceDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ChickenRaceDto update(@PathVariable Long id,
                                 @RequestBody ChickenRaceDto dto) {
        return service.update(id, dto);
    }

    @GetMapping("/test")
    public String hello() {
        return "działa";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}