package com.literandltx.timer_backend.mapper;

import com.literandltx.timer_backend.config.MapperConfig;
import com.literandltx.timer_backend.dto.timer.option.CreateTimerOptionRequestDto;
import com.literandltx.timer_backend.dto.timer.option.CreateTimerOptionResponseDto;
import com.literandltx.timer_backend.dto.timer.option.TimerOptionResponseDto;
import com.literandltx.timer_backend.model.TimerOption;
import com.literandltx.timer_backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class)
public interface TimerOptionMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", source = "user")
    })
    TimerOption toTimerOption(CreateTimerOptionRequestDto dto, User user);

    @Mapping(target = "userId", source = "user.id")
    CreateTimerOptionResponseDto toCreateTimerOptionResponse(TimerOption entity);

    @Mapping(target = "userId", source = "user.id")
    TimerOptionResponseDto toTimerOptionResponse(TimerOption entity);
}
