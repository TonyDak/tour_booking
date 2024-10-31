package com.tourbooking.tour_booking.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String user_name;
    private String email;
    private String phone_number;
    private String special_requirement;
    private LocalDate start_time;
    private LocalDate end_time;
    private Double total_price;

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

    @OneToMany(mappedBy = "bill", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Traveler> travelers;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "promotion_id",
        referencedColumnName = "id",
        nullable = true
    )
    private Promotion promotion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "promotion_id",
        referencedColumnName = "id",
        nullable = true
    )
    private Promotion promotion;


}
