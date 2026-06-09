package com.literandltx.timer_backend.dto.report;

import com.literandltx.timer_backend.model.ReportType;
import java.util.Map;
import lombok.Data;

@Data
public class ReportRequestDto {
    private ReportType reportType;
    private Map<String, Object> filters;
}
