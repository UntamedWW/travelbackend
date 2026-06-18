package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.DocumentRequest;
import com.travel.travelbackend.dto.DocumentResponse;
import com.travel.travelbackend.entity.Document;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.repository.DocumentRepository;
import com.travel.travelbackend.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TripRepository tripRepository;

    public DocumentResponse create(DocumentRequest request) {
        validateRequest(request);

        Trip trip = findTrip(request.getTripId());

        Document document = new Document();
        applyRequest(document, request, trip);

        return toResponse(documentRepository.save(document));
    }

    public DocumentResponse edit(Long id, DocumentRequest request) {
        validateRequest(request);

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        Trip trip = findTrip(request.getTripId());
        applyRequest(document, request, trip);

        return toResponse(documentRepository.save(document));
    }

    public List<DocumentResponse> getByTrip(Long tripId) {
        return documentRepository.findByTripId(tripId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));

        documentRepository.delete(document);
    }

    private Trip findTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    private void applyRequest(Document document, DocumentRequest request, Trip trip) {
        document.setName(request.getName());
        document.setNumber(request.getNumber());
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
                document.getNumber(),
                document.getType(),
                document.getUrl(),
                document.getExpirationDate(),
                tripId
        );
    }
}
