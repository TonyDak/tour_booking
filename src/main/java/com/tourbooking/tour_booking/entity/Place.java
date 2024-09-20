package com.tourbooking.tour_booking.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Entity
@Getter
@Setter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Double latitude;
    private Double longitude;
    private String address;
    private String name;

    @OneToMany(mappedBy = "place", cascade = CascadeType.PERSIST)
    private List<PlaceVisits> placeVisits;
}
