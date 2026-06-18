package com.travel.travelbackend.dto;

import com.travel.travelbackend.entity.BudgetType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BudgetRequest {
    private Long userId;
    private BudgetType type;
    private String category;
    private BigDecimal amount;
    private Long tripId;
}
