package com.example.timer_backend.repository;

import com.example.timer_backend.model.TimerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimerOptionRepository extends JpaRepository<TimerOption, Long> {
    List<TimerOption> findAllByUserId(Long userId);
}
