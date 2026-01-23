package com.example.timer_backend.service;

import com.example.timer_backend.dto.label.CreateLabelRequestDto;
import com.example.timer_backend.dto.label.CreateLabelResponseDto;
import com.example.timer_backend.dto.label.LabelRequestDto;
import com.example.timer_backend.dto.label.LabelResponseDto;
import com.example.timer_backend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LabelService {
    CreateLabelResponseDto save(CreateLabelRequestDto request, User authUser);

    LabelResponseDto findById(Long id);

    LabelResponseDto updateById(Long id, LabelRequestDto request);

    void deleteById(Long id);

    List<LabelResponseDto> findAll();
}
