package com.example.timer_backend.mapper;

import com.example.timer_backend.dto.user.UserRegistrationRequestDto;
import com.example.timer_backend.dto.user.UserRegistrationResponseDto;
import com.example.timer_backend.dto.user.UserResponseDto;
import com.example.timer_backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    User toEntity(UserRegistrationRequestDto request, String encodedPassword);

    UserRegistrationResponseDto toModel(User user);

    UserResponseDto toResponseDto(User user);
}
