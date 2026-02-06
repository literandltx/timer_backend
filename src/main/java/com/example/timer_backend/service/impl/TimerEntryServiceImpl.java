package com.example.timer_backend.service.impl;

import com.example.timer_backend.dto.timer.entry.CreateTimerEntryRequestDto;
import com.example.timer_backend.dto.timer.entry.CreateTimerEntryResponseDto;
import com.example.timer_backend.dto.timer.entry.TimerEntryRequestDto;
import com.example.timer_backend.dto.timer.entry.TimerEntryResponseDto;
import com.example.timer_backend.mapper.TimerEntryMapper;
import com.example.timer_backend.model.Label;
import com.example.timer_backend.model.TimerEntry;
import com.example.timer_backend.model.User;
import com.example.timer_backend.repository.LabelRepository;
import com.example.timer_backend.repository.TimerEntryRepository;
import com.example.timer_backend.service.TimerEntryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimerEntryServiceImpl implements TimerEntryService {
    private final TimerEntryRepository timerEntryRepository;
    private final LabelRepository labelRepository;
    private final TimerEntryMapper timerEntryMapper;

    @Override
    @Transactional
    public CreateTimerEntryResponseDto save(CreateTimerEntryRequestDto request, User authUser) {
        log.info("Creating timer entry for user id: {}", authUser.getId());

        Label label = labelRepository.findById(request.getLabelId())
                .orElseThrow(() -> new EntityNotFoundException("Label with id " + request.getLabelId() + " not found"));

        checkLabelOwnership(label, authUser);

        TimerEntry timerEntry = timerEntryMapper.toTimerEntry(request, authUser, label);
        TimerEntry savedEntry = timerEntryRepository.save(timerEntry);

        log.info("Timer entry created successfully with id: {}", savedEntry.getId());

        return timerEntryMapper.toCreateTimerEntryResponse(savedEntry);
    }

    @Override
    public TimerEntryResponseDto findById(Long id, User authUser) {
        log.info("Fetching timer entry with id: {}", id);

        TimerEntry timerEntry = timerEntryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TimerEntry with id " + id + " not found"));

        checkTimerEntryOwnership(timerEntry, authUser);

        return timerEntryMapper.toTimerEntryResponse(timerEntry);
    }

    @Override
    @Transactional
    public TimerEntryResponseDto updateById(Long id, TimerEntryRequestDto request, User authUser) {
        log.info("Updating timer entry id: {} for user id: {}", id, authUser.getId());

        TimerEntry existingEntry = timerEntryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TimerEntry with id " + id + " not found"));

        checkTimerEntryOwnership(existingEntry, authUser);

        Label label = labelRepository.findById(request.getLabelId())
                .orElseThrow(() -> new EntityNotFoundException("Label with id " + request.getLabelId() + " not found"));

        checkLabelOwnership(label, authUser);

        timerEntryMapper.updateTimerEntryFromDto(request, label, existingEntry);

        TimerEntry updatedEntry = timerEntryRepository.save(existingEntry);
        return timerEntryMapper.toTimerEntryResponse(updatedEntry);
    }

    @Override
    @Transactional
    public void deleteById(Long id, User authUser) {
        log.info("Deleting timer entry id: {} for user id: {}", id, authUser.getId());

        TimerEntry timerEntry = timerEntryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TimerEntry with id " + id + " not found"));

        checkTimerEntryOwnership(timerEntry, authUser);

        timerEntryRepository.delete(timerEntry);
        log.info("Timer entry with id: {} deleted successfully", id);
    }

    @Override
    public List<TimerEntryResponseDto> findAll(User user) {
        return timerEntryRepository.findAllByUserId(user.getId())
                .stream()
                .map(timerEntryMapper::toTimerEntryResponse)
                .toList();
    }

    private void checkTimerEntryOwnership(TimerEntry timerEntry, User authUser) {
        if (!timerEntry.getUser().getId().equals(authUser.getId())) {
            log.warn("Access denied: User {} tried to access timer entry {} owned by user {}",
                    authUser.getId(), timerEntry.getId(), timerEntry.getUser().getId());
            throw new AccessDeniedException("User does not own this timer entry");
        }
    }

    private void checkLabelOwnership(Label label, User authUser) {
        if (!label.getUser().getId().equals(authUser.getId())) {
            log.warn("Access denied: User {} tried to associate label {} owned by user {}",
                    authUser.getId(), label.getId(), label.getUser().getId());
            throw new AccessDeniedException("User does not own this label");
        }
    }
}
