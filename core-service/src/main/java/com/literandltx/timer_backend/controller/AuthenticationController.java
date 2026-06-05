package com.literandltx.timer_backend.controller;

import com.literandltx.timer_backend.dto.user.UserLoginRequestDto;
import com.literandltx.timer_backend.dto.user.UserLoginResponseDto;
import com.literandltx.timer_backend.dto.user.UserRegistrationRequestDto;
import com.literandltx.timer_backend.dto.user.UserRegistrationResponseDto;
import com.literandltx.timer_backend.dto.user.auth.ForgotPasswordRequestDto;
import com.literandltx.timer_backend.dto.user.auth.ResetPasswordRequestDto;
import com.literandltx.timer_backend.security.AuthenticationService;
import com.literandltx.timer_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto> register(@RequestBody @Valid UserRegistrationRequestDto request) {
        UserRegistrationResponseDto response = userService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/forgot")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDto request) {
        userService.processForgotPassword(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(
            @RequestParam("token") String token,
            @RequestBody @Valid ResetPasswordRequestDto request
    ) {
        userService.processResetPassword(token, request);
        return ResponseEntity.ok().build();
    }
}
