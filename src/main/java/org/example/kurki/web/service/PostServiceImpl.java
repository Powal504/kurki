package org.example.kurki.web.service;

import lombok.RequiredArgsConstructor;
import org.example.kurki.security.model.User;
import org.example.kurki.web.api.dto.PostDetailsDto;
import org.example.kurki.web.api.mapper.PostDetailsMapper;
import org.example.kurki.web.model.ChickenRace;
import org.example.kurki.web.model.Post;
import org.example.kurki.web.repository.ChickenRaceRepository;
import org.example.kurki.web.repository.PostDetailsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostDetailsRepository postRepository;
    private final ChickenRaceRepository chickenRaceRepository;
    private final PostDetailsMapper mapper;

    @Override
    public Page<PostDetailsDto> getAll(Pageable pageable) {
        return postRepository.findAllByIsBannedFalse(pageable)
                .map(mapper::toDto);
    }

    @Override
    public PostDetailsDto getOne(Integer id) {
        return mapper.toDto(getPost(id));
    }

    @Override
    public PostDetailsDto create(PostDetailsDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        post.setCreationDate(LocalDate.now());
        post.setIsBanned(false);

        post.setUser(getCurrentUser());

        post.setRaces(mapRaces(dto));

        return mapper.toDto(postRepository.save(post));
    }

    @Override
    public PostDetailsDto update(Integer id, PostDetailsDto dto) {
        Post post = getPost(id);

        if (!isOwner(post)) {
            throw new RuntimeException("Brak dostępu");
        }

        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        post.setRaces(mapRaces(dto));

        return mapper.toDto(postRepository.save(post));
    }

    @Override
    public void deleteOwn(Integer id) {
        Post post = getPost(id);

        if (!isOwner(post)) {
            throw new RuntimeException("Brak dostępu");
        }

        postRepository.delete(post);
    }

    @Override
    public void deleteAny(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public void ban(Integer id) {
        Post post = getPost(id);
        post.setIsBanned(true);
        postRepository.save(post);
    }

    private Post getPost(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private boolean isOwner(Post post) {
        return post.getUser().getId().equals(getCurrentUser().getId());
    }

    private Set<ChickenRace> mapRaces(PostDetailsDto dto) {
        return dto.getRaces().stream()
                .map(r -> chickenRaceRepository.findById(r.getId())
                        .orElseThrow())
                .collect(Collectors.toSet());
    }
}