package com.travel.travelbackend.repository;

import com.travel.travelbackend.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByTripIdAndUserId(Long tripId, Long userId);
}
