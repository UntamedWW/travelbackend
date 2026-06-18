package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.DocumentRequest;
import com.travel.travelbackend.dto.DocumentResponse;
import com.travel.travelbackend.entity.Document;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.DocumentRepository;
import com.travel.travelbackend.repository.TripRepository;
import com.travel.travelbackend.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TripRepository tripRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final DocumentStorageService documentStorageService;

    public DocumentResponse create(DocumentRequest request) {
        validateRequest(request);

        User user = authenticatedUserProvider.getCurrentUser();
        Trip trip = findCurrentUserTrip(request.getTripId(), user.getId());

        Document document = new Document();
        applyRequest(document, request, trip);

        return toResponse(documentRepository.save(document));
    }

    public DocumentResponse edit(Long id, DocumentRequest request) {
        validateRequest(request);

        User user = authenticatedUserProvider.getCurrentUser();
        Document document = documentRepository.findByIdAndTripUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        Trip trip = findCurrentUserTrip(request.getTripId(), user.getId());
        applyRequest(document, request, trip);

        return toResponse(documentRepository.save(document));
    }

    public List<DocumentResponse> getByTrip(Long tripId) {
        User user = authenticatedUserProvider.getCurrentUser();
        findCurrentUserTrip(tripId, user.getId());

        return documentRepository.findByTripIdAndTripUserId(tripId, user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DocumentResponse upload(
            Long tripId,
            String name,
            String type,
            String number,
            LocalDate expirationDate,
            MultipartFile file
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (tripId == null) {
            throw new IllegalArgumentException("Trip id is required");
        }

        User user = authenticatedUserProvider.getCurrentUser();
        Trip trip = findCurrentUserTrip(tripId, user.getId());
        String storageKey = documentStorageService.store(file, user.getId(), tripId);

        Document document = new Document();
        document.setName(name);
        document.setType(type);
        document.setNumber(number);
        document.setExpirationDate(expirationDate);
        document.setTrip(trip);
        document.setFileName(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setStorageKey(storageKey);

        Document savedDocument = documentRepository.save(document);
        savedDocument.setUrl("/api/documents/" + savedDocument.getId() + "/file");

        return toResponse(documentRepository.save(savedDocument));
    }

    public StoredDocumentFile loadFile(Long id) {
        User user = authenticatedUserProvider.getCurrentUser();
        Document document = documentRepository.findByIdAndTripUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        return new StoredDocumentFile(
                documentStorageService.load(document.getStorageKey()),
                document.getFileName(),
                document.getContentType(),
                document.getFileSize()
        );
    }

    public void delete(Long id) {
        User user = authenticatedUserProvider.getCurrentUser();
        Document document = documentRepository.findByIdAndTripUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        documentRepository.delete(document);
        documentStorageService.delete(document.getStorageKey());
    }

    private Trip findCurrentUserTrip(Long tripId, Long userId) {
        return tripRepository.findByIdAndUserId(tripId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    private void applyRequest(Document document, DocumentRequest request, Trip trip) {
        document.setName(request.getName());
        document.setNumber(request.getNumber());
        document.setType(request.getType());
        document.setUrl(request.getUrl() == null ? "" : request.getUrl());
        document.setExpirationDate(request.getExpirationDate());
        document.setTrip(trip);
    }

    private void validateRequest(DocumentRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (request.getTripId() == null) {
            throw new IllegalArgumentException("Trip id is required");
        }
    }

    private DocumentResponse toResponse(Document document) {
        Long tripId = document.getTrip() == null ? null : document.getTrip().getId();

        return new DocumentResponse(
                document.getId(),
                document.getName(),
                document.getType(),
                document.getNumber(),
                document.getUrl(),
                document.getExpirationDate(),
                tripId
        );
    }
}
