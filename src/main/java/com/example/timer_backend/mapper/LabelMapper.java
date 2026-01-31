package com.example.timer_backend.mapper;

import com.example.timer_backend.dto.label.CreateLabelRequestDto;
import com.example.timer_backend.dto.label.CreateLabelResponseDto;
import com.example.timer_backend.dto.label.LabelRequestDto;
import com.example.timer_backend.dto.label.LabelResponseDto;
import com.example.timer_backend.model.Label;
import com.example.timer_backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LabelMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    Label toLabel(CreateLabelRequestDto request, User user);

    Label toLabel(LabelRequestDto request);

    @Mapping(source = "user.id", target = "userId")
    CreateLabelResponseDto toCreateLabelResponse(Label dto);

    @Mapping(source = "user.id", target = "userId")
    LabelResponseDto toLabelResponse(Label dto);
}
