package com.example.timer_backend.service;

import com.example.timer_backend.dto.label.CreateLabelRequestDto;
import com.example.timer_backend.dto.label.CreateLabelResponseDto;
import com.example.timer_backend.dto.label.LabelRequestDto;
import com.example.timer_backend.dto.label.LabelResponseDto;
import com.example.timer_backend.mapper.LabelMapper;
import com.example.timer_backend.model.Label;
import com.example.timer_backend.model.User;
import com.example.timer_backend.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public CreateLabelResponseDto save(CreateLabelRequestDto request, User authUser) {
        Label label = labelMapper.toLabel(request);
        label.setUser(authUser);
        Label saved = labelRepository.save(label);

        return labelMapper.toCreateLabelResponse(saved);
    }

    @Override
    public LabelResponseDto findById(Long id) {
        Label label = labelRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Label not found")
        );
        return labelMapper.toLabelResponse(label);
    }

    @Override
    @Transactional
    public LabelResponseDto updateById(Long id, LabelRequestDto request) {
        Label existingLabel = labelRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Label with id " + id + " not found")
        );

        existingLabel.setName(request.getName());
        existingLabel.setColor(request.getColor());

        Label savedLabel = labelRepository.save(existingLabel);
        return labelMapper.toLabelResponse(savedLabel);
    }

    @Override
    public void deleteById(Long id) {
        if (!labelRepository.existsById(id)) {
            throw new RuntimeException("Label with id " + id + " not found");
        }
        labelRepository.deleteById(id);
    }

    public List<LabelResponseDto> findAll() {
        return labelRepository.findAll().stream()
                .map(labelMapper::toLabelResponse)
                .toList();
    }
}
