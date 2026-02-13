package com.example.timer_backend.service;

import com.example.timer_backend.dto.user.*;
import com.example.timer_backend.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto request);

    UserResponseDto getCurrentUser(User user);

    UserResponseDto updateAccount(User user, UserUpdateRequestDto request);

    void changePassword(User user, ChangePasswordRequestDto request);

    UserResponseDto changeEmail(User user, ChangeEmailRequestDto request);

    void deleteAccount(User user);
}