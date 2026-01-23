package com.example.timer_backend.mapper;

import com.example.timer_backend.dto.label.CreateLabelRequestDto;
import com.example.timer_backend.dto.label.CreateLabelResponseDto;
import com.example.timer_backend.dto.label.LabelRequestDto;
import com.example.timer_backend.dto.label.LabelResponseDto;
import com.example.timer_backend.model.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LabelMapper {
    Label toLabel(CreateLabelRequestDto request);

    Label toLabel(LabelRequestDto request);

    @Mapping(source = "user.id", target = "userId")
    CreateLabelResponseDto toCreateLabelResponse(Label dto);

    @Mapping(source = "user.id", target = "userId")
    LabelResponseDto toLabelResponse(Label dto);
}
