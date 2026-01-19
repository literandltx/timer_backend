package com.example.timer_backend.controller;

import com.example.timer_backend.dto.label.CreateLabelRequestDto;
import com.example.timer_backend.dto.label.CreateLabelResponseDto;
import com.example.timer_backend.dto.label.LabelRequestDto;
import com.example.timer_backend.dto.label.LabelResponseDto;
import com.example.timer_backend.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/labels")
public class LabelController {
    private final LabelService labelService;

    @PostMapping
    public CreateLabelResponseDto save(@RequestBody CreateLabelRequestDto request) {
        return labelService.save(request);
    }

    @GetMapping
    @ResponseBody
    public List<LabelResponseDto> findAll() {
        return labelService.findAll();
    }

    @GetMapping("/{id}")
    public LabelResponseDto findById(@PathVariable Long id) {
        return labelService.findById(id);
    }

    @PutMapping("/{id}")
    public LabelResponseDto update(@PathVariable Long id, @RequestBody LabelRequestDto request) {
        return labelService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        labelService.deleteById(id);
    }
}
