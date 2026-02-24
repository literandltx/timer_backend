package com.example.timer_backend.repository;

import com.example.timer_backend.model.TimerSetting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerSettingRepository extends JpaRepository<TimerSetting, Long> {
    Optional<TimerSetting> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
