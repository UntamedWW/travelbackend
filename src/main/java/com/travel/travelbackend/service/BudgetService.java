package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.BudgetRequest;
import com.travel.travelbackend.dto.BudgetResponse;
import com.travel.travelbackend.entity.Budget;
import com.travel.travelbackend.entity.BudgetType;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.BudgetRepository;
import com.travel.travelbackend.repository.TripRepository;
import com.travel.travelbackend.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TripRepository tripRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public BudgetResponse create(BudgetRequest request) {
        validateRequest(request);

        User user = authenticatedUserProvider.getCurrentUser();
        Trip trip = findCurrentUserTrip(request.getTripId(), user.getId());
        Budget budget = new Budget();
        applyRequest(budget, request, trip, user);

        return toResponse(budgetRepository.save(budget));
    }

    public BudgetResponse edit(Long id, BudgetRequest request) {
        validateRequest(request);

        User user = authenticatedUserProvider.getCurrentUser();
        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Budget item not found"));

        Trip trip = findCurrentUserTrip(request.getTripId(), user.getId());
        applyRequest(budget, request, trip, user);

        return toResponse(budgetRepository.save(budget));
    }

    public List<BudgetResponse> getByTrip(Long tripId) {
        if (tripId == null) {
            throw new IllegalArgumentException("Trip id is required");
        }

        User user = authenticatedUserProvider.getCurrentUser();
        findCurrentUserTrip(tripId, user.getId());

        return budgetRepository.findByTripIdAndUserId(tripId, user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long id) {
        User user = authenticatedUserProvider.getCurrentUser();
        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Budget item not found"));

        budgetRepository.delete(budget);
    }

    private Trip findCurrentUserTrip(Long tripId, Long userId) {
        return tripRepository.findByIdAndUserId(tripId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    private void applyRequest(Budget budget, BudgetRequest request, Trip trip, User user) {
        budget.setType(request.getType());
        budget.setCategory(request.getCategory());
        budget.setAmount(request.getAmount());
        budget.setTrip(trip);
        budget.setUser(user);
    }

    private void validateRequest(BudgetRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is required");
        }

        if (request.getType() == null) {
            throw new IllegalArgumentException("Budget type is required");
        }

        if (request.getAmount() == null) {
            throw new IllegalArgumentException("Amount is required");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (request.getType() == BudgetType.EXPENSE
                && (request.getCategory() == null || request.getCategory().isBlank())) {
            throw new IllegalArgumentException("Category is required for expense");
        }

        if (request.getTripId() == null) {
            throw new IllegalArgumentException("Trip id is required");
        }

    }

    private BudgetResponse toResponse(Budget budget) {
        Long tripId = budget.getTrip() == null ? null : budget.getTrip().getId();
        Long userId = budget.getUser() == null ? null : budget.getUser().getId();

        return new BudgetResponse(
                budget.getId(),
                userId,
                budget.getType(),
                budget.getCategory(),
                budget.getAmount(),
                tripId
        );
    }
}
