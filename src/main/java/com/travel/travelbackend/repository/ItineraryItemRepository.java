package com.travel.travelbackend.repository;

import com.travel.travelbackend.entity.ItineraryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItineraryItemRepository extends JpaRepository<ItineraryItem, Long> {
    List<ItineraryItem> findByTripIdAndTripUserId(Long tripId, Long userId);

    Optional<ItineraryItem> findByIdAndTripUserId(Long id, Long userId);
}
