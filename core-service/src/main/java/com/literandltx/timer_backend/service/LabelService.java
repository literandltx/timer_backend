package com.literandltx.timer_backend.service;

import com.literandltx.timer_backend.dto.label.CreateLabelRequestDto;
import com.literandltx.timer_backend.dto.label.CreateLabelResponseDto;
import com.literandltx.timer_backend.dto.label.LabelRequestDto;
import com.literandltx.timer_backend.dto.label.LabelResponseDto;
import com.literandltx.timer_backend.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface LabelService {
    CreateLabelResponseDto save(CreateLabelRequestDto request, User authUser);

    LabelResponseDto findById(Long id, User authUser);

    LabelResponseDto updateById(Long id, LabelRequestDto request, User authUser);

    void deleteById(Long id, User authUser);

    List<LabelResponseDto> findAll(User user, Pageable pageable);
}
