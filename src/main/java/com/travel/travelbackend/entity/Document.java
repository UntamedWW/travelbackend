package com.travel.travelbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String number;

    private String type;

    private String url = "";

    private String fileName;

    private String contentType;

    private Long fileSize;

    private String storageKey;

    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
