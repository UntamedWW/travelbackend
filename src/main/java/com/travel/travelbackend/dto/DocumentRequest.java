package com.travel.travelbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DocumentRequest {
    @NotBlank
    private String name;

    private String number;

    private String type;

    private String url;

    private LocalDate expirationDate;

    @NotNull
    private Long tripId;
}
