package com.example.timer_backend.controller;

import com.example.timer_backend.dto.user.*;
import com.example.timer_backend.model.User;
import com.example.timer_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(
            @AuthenticationPrincipal User user
    ) {
        UserResponseDto response = userService.getCurrentUser(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateAccount(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UserUpdateRequestDto request
    ) {
        UserResponseDto response = userService.updateAccount(user, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid ChangePasswordRequestDto request
    ) {
        userService.changePassword(user, request);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/me/email")
    public ResponseEntity<UserResponseDto> changeEmail(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid ChangeEmailRequestDto request
    ) {
        UserResponseDto response = userService.changeEmail(user, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal User user
    ) {
        userService.deleteAccount(user);
        return ResponseEntity
                .noContent()
                .build();
    }
}