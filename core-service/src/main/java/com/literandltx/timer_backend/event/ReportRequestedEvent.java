package com.literandltx.timer_backend.event;

import com.literandltx.timer_backend.model.ReportType;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestedEvent {
    private UUID reportId;
    private Long userId;
    private String email;
    private ReportType reportType;
    private Map<String, Object> filters;
}
