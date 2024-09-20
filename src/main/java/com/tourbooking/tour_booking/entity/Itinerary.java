package com.tourbooking.tour_booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import java.util.List;

@Entity
@Getter
@Setter
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private Integer day_no;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "tour_id",
        referencedColumnName = "id",
        nullable = false
    )
    private Tour tour;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.PERSIST)
    private List<PlaceVisits> placeVisits;
    
}
