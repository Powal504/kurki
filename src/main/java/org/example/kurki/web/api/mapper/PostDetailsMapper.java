package org.example.kurki.web.api.mapper;

import org.example.kurki.security.api.mapper.UserMapper;
import org.example.kurki.web.api.dto.PostDetailsDto;
import org.example.kurki.web.model.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ChickenRaceMapper.class, UserMapper.class})
public interface PostDetailsMapper {

    PostDetailsDto toDto(Post post);

}
