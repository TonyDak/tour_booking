package com.tourbooking.tour_booking.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String start_time;
    private String end_time;
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "itinerary_id",
        referencedColumnName = "id",
        nullable = true
    )
    private Itinerary itinerary;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "place_id",
        referencedColumnName = "id",
        nullable = true
    )
    private Place place;




}
