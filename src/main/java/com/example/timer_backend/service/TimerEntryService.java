package com.example.timer_backend.service;

import com.example.timer_backend.dto.timer.entry.CreateTimerEntryRequestDto;
import com.example.timer_backend.dto.timer.entry.CreateTimerEntryResponseDto;
import com.example.timer_backend.dto.timer.entry.TimerEntryRequestDto;
import com.example.timer_backend.dto.timer.entry.TimerEntryResponseDto;
import com.example.timer_backend.model.User;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface TimerEntryService {
    CreateTimerEntryResponseDto save(CreateTimerEntryRequestDto request, User authUser);

    TimerEntryResponseDto findById(Long id, User authUser);

    TimerEntryResponseDto updateById(Long id, TimerEntryRequestDto request, User authUser);

    void deleteById(Long id, User authUser);

    List<TimerEntryResponseDto> findAll(User user);
}
