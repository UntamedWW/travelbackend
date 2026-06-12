package com.travel.travelbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "packing_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean packed;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
