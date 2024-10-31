package com.tourbooking.tour_booking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;


    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    private Tour tour;







}
