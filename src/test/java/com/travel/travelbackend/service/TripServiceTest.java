package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.TripRequest;
import com.travel.travelbackend.dto.TripResponse;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.TripRepository;
import com.travel.travelbackend.repository.UserRepository;
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
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TripService tripService;

    @Test
    void shouldCreateTripSuccessfully() {
        TripRequest request = createRequest();
        User user = createUser();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(tripRepository.save(any(Trip.class)))
                .thenAnswer(invocation -> {
                    Trip trip = invocation.getArgument(0);
                    trip.setId(10L);
                    return trip;
                });

        TripResponse result = tripService.createTrip(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Summer Trip", result.getTitle());
        assertEquals("Italy", result.getDestination());
        assertEquals(1L, result.getUserId());

        verify(userRepository).findById(1L);
        verify(tripRepository).save(any(Trip.class));
    }

    @Test
    void shouldGetAllTripsSuccessfully() {
        Trip trip = createTrip();

        when(tripRepository.findAll())
                .thenReturn(List.of(trip));

        List<TripResponse> result = tripService.getAllTrips();

        assertEquals(1, result.size());
        assertEquals(10L, result.getFirst().getId());
        assertEquals("Summer Trip", result.getFirst().getTitle());
        assertEquals(1L, result.getFirst().getUserId());

        verify(tripRepository).findAll();
    }

    @Test
    void shouldGetTripByIdSuccessfully() {
        Trip trip = createTrip();

        when(tripRepository.findById(10L))
                .thenReturn(Optional.of(trip));

        TripResponse result = tripService.getTripById(10L);

        assertEquals(10L, result.getId());
        assertEquals("Summer Trip", result.getTitle());
        assertEquals("Italy", result.getDestination());

        verify(tripRepository).findById(10L);
    }

    @Test
    void shouldUpdateTripSuccessfully() {
        Trip existingTrip = createTrip();
        TripRequest request = createRequest();
        request.setTitle("Updated Trip");
        request.setDestination("Spain");
        User user = createUser();

        when(tripRepository.findById(10L))
                .thenReturn(Optional.of(existingTrip));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(tripRepository.save(existingTrip))
                .thenReturn(existingTrip);

        TripResponse result = tripService.updateTrip(10L, request);

        assertEquals(10L, result.getId());
        assertEquals("Updated Trip", result.getTitle());
        assertEquals("Spain", result.getDestination());
        assertEquals(1L, result.getUserId());

        verify(tripRepository).findById(10L);
        verify(userRepository).findById(1L);
        verify(tripRepository).save(existingTrip);
    }

    @Test
    void shouldDeleteTripSuccessfully() {
        Trip trip = createTrip();

        when(tripRepository.findById(10L))
                .thenReturn(Optional.of(trip));

        tripService.deleteTrip(10L);

        verify(tripRepository).findById(10L);
        verify(tripRepository).delete(trip);
    }

    private TripRequest createRequest() {
        TripRequest request = new TripRequest();
        request.setTitle("Summer Trip");
        request.setDestination("Italy");
        request.setStartDate(LocalDate.of(2026, 7, 1));
        request.setEndDate(LocalDate.of(2026, 7, 10));
        request.setUserId(1L);
        return request;
    }

    private Trip createTrip() {
        Trip trip = new Trip();
        trip.setId(10L);
        trip.setTitle("Summer Trip");
        trip.setDestination("Italy");
        trip.setStartDate(LocalDate.of(2026, 7, 1));
        trip.setEndDate(LocalDate.of(2026, 7, 10));
        trip.setUser(createUser());
        return trip;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("123456");
        return user;
    }
}
