package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.PackingItemRequest;
import com.travel.travelbackend.dto.PackingItemResponse;
import com.travel.travelbackend.entity.PackingItem;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.PackingItemRepository;
import com.travel.travelbackend.repository.TripRepository;
import com.travel.travelbackend.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PackingItemService {

    private final PackingItemRepository packingItemRepository;
    private final TripRepository tripRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    //add item
    public PackingItemResponse create(PackingItemRequest request){
        validateRequest(request);

        User user = authenticatedUserProvider.getCurrentUser();
        Trip trip = findCurrentUserTrip(request.getTripId(), user.getId());

        PackingItem item = new PackingItem();
        item.setName(request.getName());
        item.setPacked(false);
        item.setTrip(trip);

        return toResponse(packingItemRepository.save(item));
    }

    public PackingItemResponse edit(Long id, PackingItemRequest request){
        validateRequest(request);

        User user = authenticatedUserProvider.getCurrentUser();
        PackingItem item = packingItemRepository.findByIdAndTripUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));

        Trip trip = findCurrentUserTrip(request.getTripId(), user.getId());

        item.setName(request.getName());
        item.setTrip(trip);

        return toResponse((packingItemRepository.save(item)));
    }

    public List<PackingItemResponse> getByTrip(Long tripId){
        User user = authenticatedUserProvider.getCurrentUser();
        findCurrentUserTrip(tripId, user.getId());

        return packingItemRepository.findByTripIdAndTripUserId(tripId, user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PackingItemResponse updatePacked(Long id, boolean packed) {
        User user = authenticatedUserProvider.getCurrentUser();
        PackingItem item = packingItemRepository.findByIdAndTripUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        item.setPacked(packed);

        return toResponse(packingItemRepository.save(item));
    }

    public void delete(Long id){
        User user = authenticatedUserProvider.getCurrentUser();
        PackingItem item = packingItemRepository.findByIdAndTripUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));

        packingItemRepository.delete(item);
    }

    private Trip findCurrentUserTrip(Long tripId, Long userId) {
        return tripRepository.findByIdAndUserId(tripId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    private void validateRequest(PackingItemRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (request.getTripId() == null) {
            throw new IllegalArgumentException("Trip id is required");
        }
    }

    private PackingItemResponse toResponse(PackingItem item) {
        Long tripId = item.getTrip() == null ? null : item.getTrip().getId();

        return new PackingItemResponse(
                item.getId(),
                item.getName(),
                item.isPacked(),
                tripId
        );
    }
}
