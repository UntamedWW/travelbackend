package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.ItineraryItemRequest;
import com.travel.travelbackend.dto.ItineraryItemResponse;
import com.travel.travelbackend.entity.ItineraryItem;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.repository.ItineraryItemRepository;
import com.travel.travelbackend.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItineraryItemService {

    private final ItineraryItemRepository itineraryItemRepository;
    private final TripRepository tripRepository;

    public ItineraryItemResponse create(ItineraryItemRequest request) {
        validateRequest(request);

        Trip trip = findTrip(request.getTripId());

        ItineraryItem itineraryItem = new ItineraryItem();
        applyRequest(itineraryItem, request, trip);

        return toResponse(itineraryItemRepository.save(itineraryItem));
    }

    public ItineraryItemResponse edit(Long id, ItineraryItemRequest request) {
        validateRequest(request);

        ItineraryItem itineraryItem = itineraryItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Itinerary item not found"));

        Trip trip = findTrip(request.getTripId());
        applyRequest(itineraryItem, request, trip);

        return toResponse(itineraryItemRepository.save(itineraryItem));
    }

    public List<ItineraryItemResponse> getByTrip(Long tripId) {
        return itineraryItemRepository.findByTripId(tripId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long id) {
        ItineraryItem itineraryItem = itineraryItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Itinerary item not found"));

        itineraryItemRepository.delete(itineraryItem);
    }

    private Trip findTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    private void applyRequest(ItineraryItem itineraryItem, ItineraryItemRequest request, Trip trip) {
        itineraryItem.setName(request.getName());
        itineraryItem.setDescription(request.getDescription());
        itineraryItem.setLocation(request.getLocation());
        itineraryItem.setStartDateTime(request.getStartDateTime());
        itineraryItem.setEndDateTime(request.getEndDateTime());
        itineraryItem.setTrip(trip);
    }

    private void validateRequest(ItineraryItemRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (request.getStartDateTime() == null) {
            throw new IllegalArgumentException("Start date time is required");
        }

        if (request.getEndDateTime() != null && request.getEndDateTime().isBefore(request.getStartDateTime())) {
            throw new IllegalArgumentException("End date time must be after start date time");
        }

        if (request.getTripId() == null) {
            throw new IllegalArgumentException("Trip id is required");
        }
    }

    private ItineraryItemResponse toResponse(ItineraryItem itineraryItem) {
        Long tripId = itineraryItem.getTrip() == null ? null : itineraryItem.getTrip().getId();

        return new ItineraryItemResponse(
                itineraryItem.getId(),
                itineraryItem.getName(),
                itineraryItem.getDescription(),
                itineraryItem.getLocation(),
                itineraryItem.getStartDateTime(),
                itineraryItem.getEndDateTime(),
                tripId
        );
    }
}