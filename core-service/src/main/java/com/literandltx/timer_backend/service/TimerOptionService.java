package com.literandltx.timer_backend.service;

import com.literandltx.timer_backend.dto.timer.option.CreateTimerOptionRequestDto;
import com.literandltx.timer_backend.dto.timer.option.CreateTimerOptionResponseDto;
import com.literandltx.timer_backend.dto.timer.option.TimerOptionRequestDto;
import com.literandltx.timer_backend.dto.timer.option.TimerOptionResponseDto;
import com.literandltx.timer_backend.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TimerOptionService {
    CreateTimerOptionResponseDto save(CreateTimerOptionRequestDto request, User authUser);

    TimerOptionResponseDto findById(Long id, User authUser);

    TimerOptionResponseDto updateById(Long id, TimerOptionRequestDto request, User authUser);

    void deleteById(Long id, User authUser);

    List<TimerOptionResponseDto> findAll(User user, Pageable pageable);
}
