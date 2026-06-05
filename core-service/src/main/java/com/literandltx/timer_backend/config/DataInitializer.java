package com.literandltx.timer_backend.config;

import com.literandltx.timer_backend.model.Role;
import com.literandltx.timer_backend.model.RoleName;
import com.literandltx.timer_backend.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(
                            new Role().setName(roleName)
                    ));
        }
    }
}
