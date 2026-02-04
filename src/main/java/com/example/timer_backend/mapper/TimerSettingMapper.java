package com.example.timer_backend.mapper;

import com.example.timer_backend.dto.timerSetting.CreateTimerSettingResponseDto;
import com.example.timer_backend.dto.timerSetting.TimerSettingResponseDto;
import com.example.timer_backend.model.TimerSetting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimerSettingMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "timerOptionId", source = "preference.id")
    @Mapping(target = "value", source = "preference.value")
    CreateTimerSettingResponseDto toCreateTimerSettingResponse(TimerSetting entity);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "timerOptionId", source = "preference.id")
    @Mapping(target = "value", source = "preference.value")
    TimerSettingResponseDto toTimerSettingResponse(TimerSetting entity);
}
