package com.tourbooking.tour_booking.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BookMark> bookMarks;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

   
}
