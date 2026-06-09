package com.literandltx.timer_backend.service;

import com.literandltx.timer_backend.dto.user.ChangeEmailRequestDto;
import com.literandltx.timer_backend.dto.user.ChangePasswordRequestDto;
import com.literandltx.timer_backend.dto.user.UserRegistrationRequestDto;
import com.literandltx.timer_backend.dto.user.UserRegistrationResponseDto;
import com.literandltx.timer_backend.dto.user.UserResponseDto;
import com.literandltx.timer_backend.dto.user.UserUpdateRequestDto;
import com.literandltx.timer_backend.dto.user.auth.ForgotPasswordRequestDto;
import com.literandltx.timer_backend.dto.user.auth.ResetPasswordRequestDto;
import com.literandltx.timer_backend.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto request);

    void processForgotPassword(ForgotPasswordRequestDto request);

    void processResetPassword(String token, ResetPasswordRequestDto request);

    UserResponseDto getCurrentUser(User user);

    UserResponseDto updateAccount(User user, UserUpdateRequestDto request);

    void changePassword(User user, ChangePasswordRequestDto request);

    UserResponseDto changeEmail(User user, ChangeEmailRequestDto request);

    void deleteAccount(User user);
}
