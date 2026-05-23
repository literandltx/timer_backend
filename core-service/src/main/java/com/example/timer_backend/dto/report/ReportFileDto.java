package com.example.timer_backend.dto.report;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class ReportFileDto {
    private Resource resource;
    private String filename;
    private String contentType;
}
