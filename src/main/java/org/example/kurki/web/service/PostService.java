package org.example.kurki.web.service;

import org.example.kurki.web.api.dto.PostDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Page<PostDetailsDto> getAll(Pageable pageable);

    PostDetailsDto getOne(Integer id);

    PostDetailsDto create(PostDetailsDto dto);

    PostDetailsDto update(Integer id, PostDetailsDto dto);

    void deleteOwn(Integer id);

    void deleteAny(Integer id);

    void ban(Integer id);

}