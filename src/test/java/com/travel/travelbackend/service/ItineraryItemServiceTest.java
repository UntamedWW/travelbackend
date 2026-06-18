package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.ItineraryItemRequest;
import com.travel.travelbackend.dto.ItineraryItemResponse;
import com.travel.travelbackend.entity.ItineraryItem;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.ItineraryItemRepository;
import com.travel.travelbackend.repository.TripRepository;
import com.travel.travelbackend.security.AuthenticatedUserProvider;
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

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @InjectMocks
    private ItineraryItemService itineraryItemService;

    @Test
    void shouldCreateItineraryItemSuccessfully() {
        ItineraryItemRequest request = createRequest();
        Trip trip = createTrip();

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(createUser());
        when(tripRepository.findByIdAndUserId(1L, 2L))
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

        verify(tripRepository).findByIdAndUserId(1L, 2L);
        verify(itineraryItemRepository).save(any(ItineraryItem.class));
    }

    @Test
    void shouldEditItineraryItemSuccessfully() {
        ItineraryItem itineraryItem = createItineraryItem();
        ItineraryItemRequest request = createRequest();
        request.setName("Updated museum visit");
        Trip trip = createTrip();

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(createUser());
        when(itineraryItemRepository.findByIdAndTripUserId(10L, 2L))
                .thenReturn(Optional.of(itineraryItem));

        when(tripRepository.findByIdAndUserId(1L, 2L))
                .thenReturn(Optional.of(trip));

        when(itineraryItemRepository.save(itineraryItem))
                .thenReturn(itineraryItem);

        ItineraryItemResponse result = itineraryItemService.edit(10L, request);

        assertEquals(10L, result.getId());
        assertEquals("Updated museum visit", result.getName());
        assertEquals(1L, result.getTripId());

        verify(itineraryItemRepository).findByIdAndTripUserId(10L, 2L);
        verify(tripRepository).findByIdAndUserId(1L, 2L);
        verify(itineraryItemRepository).save(itineraryItem);
    }

    @Test
    void shouldGetItineraryItemsByTripSuccessfully() {
        ItineraryItem itineraryItem = createItineraryItem();

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(createUser());
        when(tripRepository.findByIdAndUserId(1L, 2L))
                .thenReturn(Optional.of(createTrip()));
        when(itineraryItemRepository.findByTripIdAndTripUserId(1L, 2L))
                .thenReturn(List.of(itineraryItem));

        List<ItineraryItemResponse> result = itineraryItemService.getByTrip(1L);

        assertEquals(1, result.size());
        assertEquals(10L, result.getFirst().getId());
        assertEquals("Visit museum", result.getFirst().getName());
        assertEquals(1L, result.getFirst().getTripId());

        verify(itineraryItemRepository).findByTripIdAndTripUserId(1L, 2L);
    }

    @Test
    void shouldDeleteItineraryItemSuccessfully() {
        ItineraryItem itineraryItem = createItineraryItem();

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(createUser());
        when(itineraryItemRepository.findByIdAndTripUserId(10L, 2L))
                .thenReturn(Optional.of(itineraryItem));

        itineraryItemService.delete(10L);

        verify(itineraryItemRepository).findByIdAndTripUserId(10L, 2L);
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
        trip.setUser(createUser());
        return trip;
    }

    private User createUser() {
        User user = new User();
        user.setId(2L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        return user;
    }
}
