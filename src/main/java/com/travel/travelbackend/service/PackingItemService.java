package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.PackingItemRequest;
import com.travel.travelbackend.dto.PackingItemResponse;
import com.travel.travelbackend.entity.PackingItem;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.repository.PackingItemRepository;
import com.travel.travelbackend.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PackingItemService {

    private final PackingItemRepository packingItemRepository;
    private final TripRepository tripRepository;

    //add item
    public PackingItemResponse create(PackingItemRequest request){
        validateRequest(request);

        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        PackingItem item = new PackingItem();
        item.setName(request.getName());
        item.setPacked(false);
        item.setTrip(trip);

        return toResponse(packingItemRepository.save(item));
    }

    public PackingItemResponse edit(Long id, PackingItemRequest request){
        validateRequest(request);

        PackingItem item = packingItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));

        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        item.setName(request.getName());
        item.setTrip(trip);

        return toResponse((packingItemRepository.save(item)));
    }

    public List<PackingItemResponse> getByTrip(Long tripId){
        return packingItemRepository.findByTripId(tripId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long id){
        PackingItem item = packingItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));

        packingItemRepository.delete(item);
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
