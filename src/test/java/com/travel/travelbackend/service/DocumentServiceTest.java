package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.DocumentRequest;
import com.travel.travelbackend.dto.DocumentResponse;
import com.travel.travelbackend.entity.Document;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.repository.DocumentRepository;
import com.travel.travelbackend.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void shouldCreateDocumentSuccessfully() {
        DocumentRequest request = createRequest();
        Trip trip = createTrip();

        when(tripRepository.findById(1L))
                .thenReturn(Optional.of(trip));

        when(documentRepository.save(any(Document.class)))
                .thenAnswer(invocation -> {
                    Document document = invocation.getArgument(0);
                    document.setId(10L);
                    return document;
                });

        DocumentResponse result = documentService.create(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Hotel booking", result.getName());
        assertEquals("booking", result.getType());
        assertEquals("ABC123", result.getNumber());
        assertEquals("hotel.jpg", result.getUrl());
        assertEquals(1L, result.getTripId());

        verify(tripRepository).findById(1L);
        verify(documentRepository).save(any(Document.class));
    }

    @Test
    void shouldEditDocumentSuccessfully() {
        Document document = createDocument();
        DocumentRequest request = createRequest();
        request.setName("Updated booking");
        request.setNumber("XYZ789");
        request.setType("insurance");
        Trip trip = createTrip();

        when(documentRepository.findById(10L))
                .thenReturn(Optional.of(document));

        when(tripRepository.findById(1L))
                .thenReturn(Optional.of(trip));

        when(documentRepository.save(document))
                .thenReturn(document);

        DocumentResponse result = documentService.edit(10L, request);

        assertEquals(10L, result.getId());
        assertEquals("Updated booking", result.getName());
        assertEquals("insurance", result.getType());
        assertEquals("XYZ789", result.getNumber());
        assertEquals(1L, result.getTripId());

        verify(documentRepository).findById(10L);
        verify(tripRepository).findById(1L);
        verify(documentRepository).save(document);
    }

    @Test
    void shouldGetDocumentsByTripSuccessfully() {
        Document document = createDocument();

        when(documentRepository.findByTripId(1L))
                .thenReturn(List.of(document));

        List<DocumentResponse> result = documentService.getByTrip(1L);

        assertEquals(1, result.size());
        assertEquals(10L, result.getFirst().getId());
        assertEquals("Hotel booking", result.getFirst().getName());
        assertEquals("booking", result.getFirst().getType());
        assertEquals(1L, result.getFirst().getTripId());

        verify(documentRepository).findByTripId(1L);
    }

    @Test
    void shouldDeleteDocumentSuccessfully() {
        Document document = createDocument();

        when(documentRepository.findById(10L))
                .thenReturn(Optional.of(document));

        documentService.delete(10L);

        verify(documentRepository).findById(10L);
        verify(documentRepository).delete(document);
    }

    private DocumentRequest createRequest() {
        DocumentRequest request = new DocumentRequest();
        request.setName("Hotel booking");
        request.setNumber("ABC123");
        request.setType("booking");
        request.setUrl("hotel.jpg");
        request.setExpirationDate(LocalDate.of(2026, 8, 15));
        request.setTripId(1L);
        return request;
    }

    private Document createDocument() {
        Document document = new Document();
        document.setId(10L);
        document.setName("Hotel booking");
        document.setNumber("ABC123");
        document.setType("booking");
        document.setUrl("hotel.jpg");
        document.setExpirationDate(LocalDate.of(2026, 8, 15));
        document.setTrip(createTrip());
        return document;
    }

    private Trip createTrip() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setTitle("Summer Trip");
        trip.setDestination("Italy");
        trip.setStartDate(LocalDate.of(2026, 7, 1));
        trip.setEndDate(LocalDate.of(2026, 7, 10));
        return trip;
    }
}
