package org.example.kurki.security.api.mapper;

import org.example.kurki.security.api.dto.LoginUserDto;
import org.example.kurki.security.api.dto.RegisterUserDto;
import org.example.kurki.security.api.dto.ResetPasswordDTO;
import org.example.kurki.security.api.dto.VerifyUserDto;
import org.example.kurki.security.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterUserDto dto);
}
