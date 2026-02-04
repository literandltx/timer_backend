package com.example.timer_backend.service;

import com.example.timer_backend.dto.timerSetting.CreateTimerSettingRequestDto;
import com.example.timer_backend.dto.timerSetting.CreateTimerSettingResponseDto;
import com.example.timer_backend.dto.timerSetting.TimerSettingRequestDto;
import com.example.timer_backend.dto.timerSetting.TimerSettingResponseDto;
import com.example.timer_backend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TimerSettingService {
    CreateTimerSettingResponseDto save(CreateTimerSettingRequestDto request, User authUser);

    TimerSettingResponseDto findById(Long id, User authUser);

    TimerSettingResponseDto updateById(Long id, TimerSettingRequestDto request, User authUser);

    void deleteById(Long id, User authUser);

    List<TimerSettingResponseDto> findAll(User user);
}