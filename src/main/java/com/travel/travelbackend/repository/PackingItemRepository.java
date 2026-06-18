package com.travel.travelbackend.repository;

import com.travel.travelbackend.entity.PackingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackingItemRepository extends JpaRepository<PackingItem, Long> {
    List<PackingItem> findByTripIdAndTripUserId(Long tripId, Long userId);

    Optional<PackingItem> findByIdAndTripUserId(Long id, Long userId);
}
