package com.tourbooking.tour_booking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class Traveler {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String phone_number;
    private String name;

    @Enumerated(EnumType.STRING)
    private TravelerType type;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "bill_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Bill bill;

    public enum TravelerType {
        ADULT,
        CHILD,
        INFANT
    }
}
