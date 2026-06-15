package com.travel.travelbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PackingItemResponse {
    private Long id;
    private String name;
    private boolean packed;
    private Long tripId;
}
