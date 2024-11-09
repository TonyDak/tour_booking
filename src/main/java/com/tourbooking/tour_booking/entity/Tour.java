package com.tourbooking.tour_booking.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
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
    private String slug;
    private String avt;
    @Column(length = 2000)
    private String highlight;
    private Double price;
    private Integer total_days;
    private LocalDate start_days;
    private LocalDate end_days;
    private LocalDate bookable_start_date;
    private LocalDate bookable_end_date;
    @Column(length = 2000)
    private String policy;
    private Integer min_booking_traveller;

    @OneToOne(mappedBy = "tour", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
    private Bill bill;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Gallery> galleries;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Itinerary> itineraries;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<BookMark> bookMarks;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

   
}
