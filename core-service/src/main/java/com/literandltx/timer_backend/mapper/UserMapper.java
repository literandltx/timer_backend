package com.literandltx.timer_backend.mapper;

import com.literandltx.timer_backend.config.MapperConfig;
import com.literandltx.timer_backend.dto.user.UserRegistrationRequestDto;
import com.literandltx.timer_backend.dto.user.UserRegistrationResponseDto;
import com.literandltx.timer_backend.dto.user.UserResponseDto;
import com.literandltx.timer_backend.model.Role;
import com.literandltx.timer_backend.model.User;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "password", source = "encodedPassword"),
            @Mapping(target = "roles", source = "userRole")
    })
    User toEntity(UserRegistrationRequestDto request, String encodedPassword, Set<Role> userRole);

    UserRegistrationResponseDto toModel(User user);

    UserResponseDto toResponseDto(User user);
}
