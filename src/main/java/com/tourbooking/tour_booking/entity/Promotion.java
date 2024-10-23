package com.tourbooking.tour_booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String code;
    private String title;
    @Lob
    private String content;
    private String thumbnail;
    private LocalDate expireddate;

    @OneToOne(mappedBy = "promotion", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
    private Bill bill;

    @ManyToMany(mappedBy = "promotions")
    private Set<User> users;




}
