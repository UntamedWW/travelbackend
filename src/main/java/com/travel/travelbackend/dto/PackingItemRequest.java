package com.travel.travelbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackingItemRequest {
    private String name;
    private Long tripId;
}
