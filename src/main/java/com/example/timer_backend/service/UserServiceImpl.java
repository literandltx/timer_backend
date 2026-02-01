package com.example.timer_backend.service;

import com.example.timer_backend.dto.user.UserRegistrationRequestDto;
import com.example.timer_backend.dto.user.UserRegistrationResponseDto;
import com.example.timer_backend.exception.custom.UserAlreadyExistsException;
import com.example.timer_backend.mapper.UserMapper;
import com.example.timer_backend.model.User;
import com.example.timer_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Unable to complete registration. User already exists.");
        }

        User user = userMapper.toEntity(request, passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);

        return userMapper.toModel(saved);
    }
}
