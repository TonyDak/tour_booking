package com.tourbooking.tour_booking.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bill {
    @Id
    private String id;
    private LocalDateTime booked_at = LocalDateTime.now();
    private String cancellation_reason;
    private String special_requirement;
    private LocalDate start_time = LocalDate.now();
    private Integer total_price;
    //status: draft, pending, paid, canceled
    @Enumerated(EnumType.STRING)
    private BillStatus bill_status;

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


    public enum BillStatus {
        DRAFT,
        PENDING,
        PAID,
        FAILED,
        CANCELLED;

        public String getName() {
            return this.name();
        }
    }
}

