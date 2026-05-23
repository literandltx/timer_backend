package com.example.timer_backend.event;

import com.example.timer_backend.model.ReportStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportStatusEvent {
    private UUID reportId;
    private ReportStatus reportStatus;
    private String s3Key;
    private String errorMessage;
}
