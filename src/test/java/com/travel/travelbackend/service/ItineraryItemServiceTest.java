package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.ItineraryItemRequest;
import com.travel.travelbackend.dto.ItineraryItemResponse;
import com.travel.travelbackend.entity.ItineraryItem;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.repository.ItineraryItemRepository;
import com.travel.travelbackend.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItineraryItemServiceTest {

    @Mock
    private ItineraryItemRepository itineraryItemRepository;

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private ItineraryItemService itineraryItemService;

    @Test
    void shouldCreateItineraryItemSuccessfully() {
        ItineraryItemRequest request = createRequest();
        Trip trip = createTrip();

        when(tripRepository.findById(1L))
                .thenReturn(Optional.of(trip));

        when(itineraryItemRepository.save(any(ItineraryItem.class)))
                .thenAnswer(invocation -> {
                    ItineraryItem itineraryItem = invocation.getArgument(0);
                    itineraryItem.setId(10L);
                    return itineraryItem;
                });

        ItineraryItemResponse result = itineraryItemService.create(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Visit museum", result.getName());
        assertEquals("City museum tour", result.getDescription());
        assertEquals("Rome", result.getLocation());
        assertEquals(1L, result.getTripId());

        verify(tripRepository).findById(1L);
        verify(itineraryItemRepository).save(any(ItineraryItem.class));
    }

    @Test
    void shouldEditItineraryItemSuccessfully() {
        ItineraryItem itineraryItem = createItineraryItem();
        ItineraryItemRequest request = createRequest();
        request.setName("Updated museum visit");
        Trip trip = createTrip();

        when(itineraryItemRepository.findById(10L))
                .thenReturn(Optional.of(itineraryItem));

        when(tripRepository.findById(1L))
                .thenReturn(Optional.of(trip));

        when(itineraryItemRepository.save(itineraryItem))
                .thenReturn(itineraryItem);

        ItineraryItemResponse result = itineraryItemService.edit(10L, request);

        assertEquals(10L, result.getId());
        assertEquals("Updated museum visit", result.getName());
        assertEquals(1L, result.getTripId());

        verify(itineraryItemRepository).findById(10L);
        verify(tripRepository).findById(1L);
        verify(itineraryItemRepository).save(itineraryItem);
    }

    @Test
    void shouldGetItineraryItemsByTripSuccessfully() {
        ItineraryItem itineraryItem = createItineraryItem();

        when(itineraryItemRepository.findByTripId(1L))
                .thenReturn(List.of(itineraryItem));

        List<ItineraryItemResponse> result = itineraryItemService.getByTrip(1L);

        assertEquals(1, result.size());
        assertEquals(10L, result.getFirst().getId());
        assertEquals("Visit museum", result.getFirst().getName());
        assertEquals(1L, result.getFirst().getTripId());

        verify(itineraryItemRepository).findByTripId(1L);
    }

    @Test
    void shouldDeleteItineraryItemSuccessfully() {
        ItineraryItem itineraryItem = createItineraryItem();

        when(itineraryItemRepository.findById(10L))
                .thenReturn(Optional.of(itineraryItem));

        itineraryItemService.delete(10L);

        verify(itineraryItemRepository).findById(10L);
        verify(itineraryItemRepository).delete(itineraryItem);
    }

    private ItineraryItemRequest createRequest() {
        ItineraryItemRequest request = new ItineraryItemRequest();
        request.setName("Visit museum");
        request.setDescription("City museum tour");
        request.setLocation("Rome");
        request.setStartDateTime(LocalDateTime.of(2026, 7, 2, 10, 0));
        request.setEndDateTime(LocalDateTime.of(2026, 7, 2, 12, 0));
        request.setTripId(1L);
        return request;
    }

    private ItineraryItem createItineraryItem() {
        ItineraryItem itineraryItem = new ItineraryItem();
        itineraryItem.setId(10L);
        itineraryItem.setName("Visit museum");
        itineraryItem.setDescription("City museum tour");
        itineraryItem.setLocation("Rome");
        itineraryItem.setStartDateTime(LocalDateTime.of(2026, 7, 2, 10, 0));
        itineraryItem.setEndDateTime(LocalDateTime.of(2026, 7, 2, 12, 0));
        itineraryItem.setTrip(createTrip());
        return itineraryItem;
    }

    private Trip createTrip() {
        Trip trip = new Trip();
        trip.setId(1L);
        return trip;
    }
}
