package org.example.kurki.security.api.mapper;

import org.example.kurki.security.api.dto.RegisterUserDto;
import org.example.kurki.security.api.dto.UserResponseDto;
import org.example.kurki.security.model.User;
import org.example.kurki.web.api.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "name")
    User toEntity(RegisterUserDto dto);

    UserDto toDto(User user);

    UserResponseDto toResponseDto(User user);
}