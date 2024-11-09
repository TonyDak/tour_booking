package com.tourbooking.tour_booking.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @Column(length = 2000)
    private String description;
    private Integer order_number;

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
