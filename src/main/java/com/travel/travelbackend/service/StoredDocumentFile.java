package com.travel.travelbackend.service;

import org.springframework.core.io.Resource;

public record StoredDocumentFile(
        Resource resource,
        String fileName,
        String contentType,
        Long fileSize
) {
}
