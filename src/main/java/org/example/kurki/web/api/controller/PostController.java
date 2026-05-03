package org.example.kurki.web.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.kurki.web.api.dto.PostDetailsDto;
import org.example.kurki.web.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public Page<PostDetailsDto> getAll(Pageable pageable) {
        return postService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public PostDetailsDto getOne(@PathVariable Integer id) {
        return postService.getOne(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public PostDetailsDto create(@RequestBody PostDetailsDto dto) {
        return postService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public PostDetailsDto update(@PathVariable Integer id,
                                 @RequestBody PostDetailsDto dto) {
        return postService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public void deleteOwn(@PathVariable Integer id) {
        postService.deleteOwn(id);
    }

    @PatchMapping("/{id}/ban")
    @PreAuthorize("hasRole('MODERATOR')")
    public void ban(@PathVariable Integer id) {
        postService.ban(id);
    }

    @DeleteMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAny(@PathVariable Integer id) {
        postService.deleteAny(id);
    }
}
