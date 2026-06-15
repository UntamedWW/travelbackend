package com.travel.travelbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TripRequest {
    private String title;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long userId;
}
