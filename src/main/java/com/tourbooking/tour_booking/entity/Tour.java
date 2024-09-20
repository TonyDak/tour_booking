package com.tourbooking.tour_booking.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String avt;
    private Double price;
    private Integer total_days;
    private LocalDate start_days;
    private LocalDate end_days;
    private LocalDate bookable_start_date;
    private LocalDate bookable_end_date;
    private String policy;
    private Integer min_booking_traveller;

    @OneToOne(mappedBy = "tour", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
    private Bill bill;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Gallery> galleries;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Itinerary> itineraries;
}
