package com.example.timer_backend.mapper;

import com.example.timer_backend.dto.timer.option.CreateTimerOptionRequestDto;
import com.example.timer_backend.dto.timer.option.CreateTimerOptionResponseDto;
import com.example.timer_backend.dto.timer.option.TimerOptionResponseDto;
import com.example.timer_backend.model.TimerOption;
import com.example.timer_backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimerOptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    TimerOption toTimerOption(CreateTimerOptionRequestDto dto, User user);

    @Mapping(target = "userId", source = "user.id")
    CreateTimerOptionResponseDto toCreateTimerOptionResponse(TimerOption entity);

    @Mapping(target = "userId", source = "user.id")
    TimerOptionResponseDto toTimerOptionResponse(TimerOption entity);
}
