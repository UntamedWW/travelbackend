package com.travel.travelbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class DocumentStorageService {

    private final Path rootLocation;

    public DocumentStorageService(
            @Value("${app.documents.storage-location:uploads/documents}") String storageLocation
    ) {
        this.rootLocation = Path.of(storageLocation).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file, Long userId, Long tripId) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null
                ? "document"
                : file.getOriginalFilename());

        if (originalName.contains("..")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String storageKey = userId + "/" + tripId + "/" + UUID.randomUUID() + "-" + originalName;
        Path destination = rootLocation.resolve(storageKey).normalize();

        if (!destination.startsWith(rootLocation)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        try {
            Files.createDirectories(destination.getParent());
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            return storageKey;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not store document file", exception);
        }
    }

    public Resource load(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            throw new IllegalArgumentException("Document file not found");
        }

        Path file = rootLocation.resolve(storageKey).normalize();
        if (!file.startsWith(rootLocation) || !Files.exists(file)) {
            throw new IllegalArgumentException("Document file not found");
        }

        try {
            return new UrlResource(file.toUri());
        } catch (MalformedURLException exception) {
            throw new IllegalStateException("Could not load document file", exception);
        }
    }

    public void delete(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return;
        }

        Path file = rootLocation.resolve(storageKey).normalize();
        if (!file.startsWith(rootLocation)) {
            return;
        }

        try {
            Files.deleteIfExists(file);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not delete document file", exception);
        }
    }
}
