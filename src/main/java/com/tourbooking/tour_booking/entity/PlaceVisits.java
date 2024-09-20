package com.tourbooking.tour_booking.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class PlaceVisits {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private String desccription;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "itinerary_id",
        referencedColumnName = "id",
        nullable = true
    )
    private Itinerary itinerary;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "place_id",
        referencedColumnName = "id",
        nullable = true
    )
    private Place place;
}
