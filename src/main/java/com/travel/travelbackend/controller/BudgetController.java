package com.travel.travelbackend.controller;

import com.travel.travelbackend.dto.BudgetRequest;
import com.travel.travelbackend.dto.BudgetResponse;
import com.travel.travelbackend.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponse create(@RequestBody BudgetRequest request) {
        return budgetService.create(request);
    }

    @PutMapping("/{id}")
    public BudgetResponse edit(@PathVariable Long id, @RequestBody BudgetRequest request) {
        return budgetService.edit(id, request);
    }

    @GetMapping("/trip/{tripId}/user/{userId}")
    public List<BudgetResponse> getByTripAndUser(@PathVariable Long tripId, @PathVariable Long userId) {
        return budgetService.getByTripAndUser(tripId, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        budgetService.delete(id);
    }
}
