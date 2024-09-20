package com.tourbooking.tour_booking.entity;

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
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String extensions;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "tour_id",
        referencedColumnName = "id",
        nullable = false
    )
    private Tour tour;
}
