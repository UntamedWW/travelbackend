package com.travel.travelbackend.dto;

import com.travel.travelbackend.entity.BudgetType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BudgetRequest {
    @NotNull
    private BudgetType type;

    private String category;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private Long tripId;
}
