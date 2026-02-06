package com.example.timer_backend.repository;

import com.example.timer_backend.model.TimerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimerEntryRepository extends JpaRepository<TimerEntry, Long> {
    List<TimerEntry> findAllByUserId(Long userId);
}
