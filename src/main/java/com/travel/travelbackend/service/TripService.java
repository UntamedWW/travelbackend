package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.TripRequest;
import com.travel.travelbackend.dto.TripResponse;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.TripRepository;
import com.travel.travelbackend.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TripService {

    private final TripRepository tripRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public TripResponse createTrip(TripRequest request) {
        User currentUser = authenticatedUserProvider.getCurrentUser();
        Trip trip = new Trip();
        applyRequest(trip, request, currentUser);

        return toResponse(tripRepository.save(trip));
    }

    public List<TripResponse> getAllTrips() {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        return tripRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TripResponse getTripById(Long id) {
        return toResponse(findCurrentUserTrip(id));
    }

    public TripResponse updateTrip(Long id, TripRequest request) {
        User currentUser = authenticatedUserProvider.getCurrentUser();
        Trip trip = findCurrentUserTrip(id, currentUser.getId());
        applyRequest(trip, request, currentUser);

        return toResponse(tripRepository.save(trip));
    }

    public void deleteTrip(Long id) {
        Trip trip = findCurrentUserTrip(id);
        tripRepository.delete(trip);
    }

    private Trip findCurrentUserTrip(Long id) {
        User currentUser = authenticatedUserProvider.getCurrentUser();
        return findCurrentUserTrip(id, currentUser.getId());
    }

    private Trip findCurrentUserTrip(Long id, Long userId) {
        return tripRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    private void applyRequest(Trip trip, TripRequest request, User user) {
        validateRequest(request);

        trip.setTitle(request.getTitle());
        trip.setDestination(request.getDestination());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setUser(user);
    }

    private void validateRequest(TripRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (request.getDestination() == null || request.getDestination().isBlank()) {
            throw new IllegalArgumentException("Destination is required");
        }

        if (request.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }

        if (request.getEndDate() == null) {
            throw new IllegalArgumentException("End date is required");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

    }

    private TripResponse toResponse(Trip trip) {
        Long userId = trip.getUser() == null ? null : trip.getUser().getId();

        return new TripResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                userId
        );
    }
}
