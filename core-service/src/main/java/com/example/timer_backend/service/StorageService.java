package com.example.timer_backend.service;

import org.springframework.core.io.Resource;

public interface StorageService {
    Resource downloadFile(String fileKey);
}
