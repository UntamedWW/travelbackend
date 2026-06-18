package com.travel.travelbackend.repository;

import com.travel.travelbackend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByTripIdAndTripUserId(Long tripId, Long userId);

    Optional<Document> findByIdAndTripUserId(Long id, Long userId);
}
