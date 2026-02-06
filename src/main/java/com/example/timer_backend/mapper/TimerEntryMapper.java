package com.example.timer_backend.mapper;

import com.example.timer_backend.dto.timer.entry.CreateTimerEntryRequestDto;
import com.example.timer_backend.dto.timer.entry.CreateTimerEntryResponseDto;
import com.example.timer_backend.dto.timer.entry.TimerEntryRequestDto;
import com.example.timer_backend.dto.timer.entry.TimerEntryResponseDto;
import com.example.timer_backend.model.Label;
import com.example.timer_backend.model.TimerEntry;
import com.example.timer_backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimerEntryMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "labelId", source = "label.id")
    TimerEntryResponseDto toTimerEntryResponse(TimerEntry entity);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "labelId", source = "label.id")
    CreateTimerEntryResponseDto toCreateTimerEntryResponse(TimerEntry entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "label", source = "label")
    @Mapping(target = "durationSeconds", source = "dto.durationSeconds")
    @Mapping(target = "startTime", source = "dto.startTime")
    TimerEntry toTimerEntry(CreateTimerEntryRequestDto dto, User user, Label label);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User ownership usually doesn't change on edit
    @Mapping(target = "label", source = "label")
    @Mapping(target = "durationSeconds", source = "dto.durationSeconds")
    @Mapping(target = "startTime", source = "dto.startTime")
    void updateTimerEntryFromDto(TimerEntryRequestDto dto, Label label, @MappingTarget TimerEntry entity);
}