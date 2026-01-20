package com.example.timer_backend.controller;

import com.example.timer_backend.dto.user.UserLoginRequestDto;
import com.example.timer_backend.dto.user.UserLoginResponseDto;
import com.example.timer_backend.dto.user.UserRegistrationRequestDto;
import com.example.timer_backend.dto.user.UserRegistrationResponseDto;
import com.example.timer_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody final UserLoginRequestDto request) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/register")
    public UserRegistrationResponseDto register(@RequestBody final UserRegistrationRequestDto request) {
        return userService.register(request);
    }
}
