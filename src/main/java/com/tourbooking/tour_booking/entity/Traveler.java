package com.tourbooking.tour_booking.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "traveler", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Bill> bills;
}
