package com.example.timer_backend.controller;

import com.example.timer_backend.dto.timer.entry.CreateTimerEntryRequestDto;
import com.example.timer_backend.dto.timer.entry.CreateTimerEntryResponseDto;
import com.example.timer_backend.dto.timer.entry.TimerEntryRequestDto;
import com.example.timer_backend.dto.timer.entry.TimerEntryResponseDto;
import com.example.timer_backend.model.User;
import com.example.timer_backend.service.TimerEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/timer-entries")
public class TimerEntryController {
    private final TimerEntryService timerEntryService;

    @PostMapping
    public ResponseEntity<CreateTimerEntryResponseDto> save(
            @RequestBody @Valid CreateTimerEntryRequestDto request,
            @AuthenticationPrincipal User user
    ) {
        CreateTimerEntryResponseDto response = timerEntryService.save(request, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<TimerEntryResponseDto>> findAll(@AuthenticationPrincipal User user) {
        List<TimerEntryResponseDto> response = timerEntryService.findAll(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimerEntryResponseDto> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        TimerEntryResponseDto response = timerEntryService.findById(id, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimerEntryResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid TimerEntryRequestDto request,
            @AuthenticationPrincipal User user
    ) {
        TimerEntryResponseDto response = timerEntryService.updateById(id, request, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        timerEntryService.deleteById(id, user);
        return ResponseEntity
                .noContent()
                .build();
    }
}
