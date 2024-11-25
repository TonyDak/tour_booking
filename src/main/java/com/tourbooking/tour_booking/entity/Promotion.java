package com.tourbooking.tour_booking.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    @ManyToMany(mappedBy = "promotions")
    private Set<Bill> bills = new HashSet<>();

    public double calculateDiscount(double totalBeforeDiscount) {
        if (this.type == 0) {
            double percentageAmount = totalBeforeDiscount * (this.discount / 100.0);
            return Math.min(percentageAmount, this.max_discount);
        } else if (this.type == 1) {
            return Math.min(this.discount, this.max_discount);
        }
        return 0;
    }

}
