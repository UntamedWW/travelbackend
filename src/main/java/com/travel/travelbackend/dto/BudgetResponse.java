package com.travel.travelbackend.dto;

import com.travel.travelbackend.entity.BudgetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BudgetResponse {
    private Long id;
    private Long userId;
    private BudgetType type;
    private String category;
    private BigDecimal amount;
    private Long tripId;
}
