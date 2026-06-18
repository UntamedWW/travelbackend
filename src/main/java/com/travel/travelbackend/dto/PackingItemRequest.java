package com.travel.travelbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackingItemRequest {
    @NotBlank
    private String name;

    @NotNull
    private Long tripId;
}
