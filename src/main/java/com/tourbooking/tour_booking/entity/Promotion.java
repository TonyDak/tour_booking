package com.tourbooking.tour_booking.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer active;
    private String code;
    private String description;
    private Integer discount;
    private Integer max_discount;
    private Integer min_order;
    private String name;
    private Integer type;
    private Integer stock;
    private LocalDate start_time;
    private LocalDate end_time;

    //bill
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.PERSIST)
    private List<Bill> bills;
}
