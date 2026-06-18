package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.BudgetRequest;
import com.travel.travelbackend.dto.BudgetResponse;
import com.travel.travelbackend.entity.Budget;
import com.travel.travelbackend.entity.BudgetType;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.BudgetRepository;
import com.travel.travelbackend.repository.TripRepository;
import com.travel.travelbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    void shouldCreateBudgetSuccessfully() {
        BudgetRequest request = createRequest(BudgetType.BUDGET);
        Trip trip = createTrip();
        User user = createUser();

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(tripRepository.findById(1L))
                .thenReturn(Optional.of(trip));

        when(budgetRepository.save(any(Budget.class)))
                .thenAnswer(invocation -> {
                    Budget budget = invocation.getArgument(0);
                    budget.setId(10L);
                    return budget;
                });

        BudgetResponse result = budgetService.create(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(2L, result.getUserId());
        assertEquals(BudgetType.BUDGET, result.getType());
        assertEquals(new BigDecimal("5000.00"), result.getAmount());
        assertEquals(1L, result.getTripId());

        verify(userRepository).findById(2L);
        verify(tripRepository).findById(1L);
        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    void shouldCreateExpenseSuccessfully() {
        BudgetRequest request = createRequest(BudgetType.EXPENSE);
        request.setCategory("car");
        request.setAmount(new BigDecimal("50.00"));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(createUser()));

        when(tripRepository.findById(1L))
                .thenReturn(Optional.of(createTrip()));

        when(budgetRepository.save(any(Budget.class)))
                .thenAnswer(invocation -> {
                    Budget budget = invocation.getArgument(0);
                    budget.setId(11L);
                    return budget;
                });

        BudgetResponse result = budgetService.create(request);

        assertEquals(11L, result.getId());
        assertEquals(BudgetType.EXPENSE, result.getType());
        assertEquals("car", result.getCategory());
        assertEquals(new BigDecimal("50.00"), result.getAmount());
    }

    @Test
    void shouldEditBudgetSuccessfully() {
        Budget budget = createBudget();
        BudgetRequest request = createRequest(BudgetType.EXPENSE);
        request.setCategory("hotel");
        request.setAmount(new BigDecimal("250.00"));

        when(budgetRepository.findById(10L))
                .thenReturn(Optional.of(budget));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(createUser()));

        when(tripRepository.findById(1L))
                .thenReturn(Optional.of(createTrip()));

        when(budgetRepository.save(budget))
                .thenReturn(budget);

        BudgetResponse result = budgetService.edit(10L, request);

        assertEquals(10L, result.getId());
        assertEquals(BudgetType.EXPENSE, result.getType());
        assertEquals("hotel", result.getCategory());
        assertEquals(new BigDecimal("250.00"), result.getAmount());

        verify(budgetRepository).findById(10L);
        verify(userRepository).findById(2L);
        verify(tripRepository).findById(1L);
        verify(budgetRepository).save(budget);
    }

    @Test
    void shouldGetBudgetsByTripAndUserSuccessfully() {
        Budget budget = createBudget();

        when(budgetRepository.findByTripIdAndUserId(1L, 2L))
                .thenReturn(List.of(budget));

        List<BudgetResponse> result = budgetService.getByTripAndUser(1L, 2L);

        assertEquals(1, result.size());
        assertEquals(10L, result.getFirst().getId());
        assertEquals(2L, result.getFirst().getUserId());
        assertEquals(1L, result.getFirst().getTripId());

        verify(budgetRepository).findByTripIdAndUserId(1L, 2L);
    }

    @Test
    void shouldDeleteBudgetSuccessfully() {
        Budget budget = createBudget();

        when(budgetRepository.findById(10L))
                .thenReturn(Optional.of(budget));

        budgetService.delete(10L);

        verify(budgetRepository).findById(10L);
        verify(budgetRepository).delete(budget);
    }

    @Test
    void shouldRejectExpenseWithoutCategory() {
        BudgetRequest request = createRequest(BudgetType.EXPENSE);
        request.setCategory("");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> budgetService.create(request)
        );

        assertEquals("Category is required for expense", exception.getMessage());
    }

    @Test
    void shouldRejectNegativeAmount() {
        BudgetRequest request = createRequest(BudgetType.BUDGET);
        request.setAmount(new BigDecimal("-1.00"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> budgetService.create(request)
        );

        assertEquals("Amount must be greater than zero", exception.getMessage());
    }

    private BudgetRequest createRequest(BudgetType type) {
        BudgetRequest request = new BudgetRequest();
        request.setUserId(2L);
        request.setType(type);
        request.setCategory(null);
        request.setAmount(new BigDecimal("5000.00"));
        request.setTripId(1L);
        return request;
    }

    private Budget createBudget() {
        Budget budget = new Budget();
        budget.setId(10L);
        budget.setType(BudgetType.BUDGET);
        budget.setCategory(null);
        budget.setAmount(new BigDecimal("5000.00"));
        budget.setTrip(createTrip());
        budget.setUser(createUser());
        return budget;
    }

    private Trip createTrip() {
        Trip trip = new Trip();
        trip.setId(1L);
        return trip;
    }

    private User createUser() {
        User user = new User();
        user.setId(2L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        return user;
    }
}
