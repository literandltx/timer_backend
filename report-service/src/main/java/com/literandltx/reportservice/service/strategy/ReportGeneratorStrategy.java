package com.literandltx.reportservice.service.strategy;

import com.literandltx.reportservice.event.ReportRequestedEvent;
import com.literandltx.reportservice.event.ReportType;

public interface ReportGeneratorStrategy {
    byte[] generate(ReportRequestedEvent event);

    ReportType getReportType();
}
