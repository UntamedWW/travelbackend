package com.travel.travelbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DocumentRequest {
    private String name;
    private String number;
    private String type;
    private String url;
    private LocalDate expirationDate;
    private Long tripId;
}
