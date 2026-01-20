package com.example.timer_backend.mapper;

import com.example.timer_backend.dto.user.UserRegistrationResponseDto;
import com.example.timer_backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserRegistrationResponseDto toModel(User user);
}
