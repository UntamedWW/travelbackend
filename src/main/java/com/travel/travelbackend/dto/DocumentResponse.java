package com.travel.travelbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DocumentResponse {
    private Long id;
    private String name;
    private String number;
    private String url;
    private LocalDate expirationDate;
    private Long tripId;
}
