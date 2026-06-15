package com.travel.travelbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "itinerary_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String location;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
