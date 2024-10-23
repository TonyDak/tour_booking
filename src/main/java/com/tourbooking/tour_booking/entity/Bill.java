package com.tourbooking.tour_booking.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate start_time;
    private LocalDate end_time;

    @ManyToOne(cascade = CascadeType.PERSIST)   
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "id",
        nullable = true        
    )
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "tour_id",
        referencedColumnName = "id",
        nullable = false
    )
    private Tour tour;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "traveler_id",
        referencedColumnName = "id",
        nullable = false
    )
    private Traveler traveler;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "promotion_id",
        referencedColumnName = "id",
        nullable = true
    )
    private Promotion promotion;


}
