package com.tourbooking.tour_booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_traveller")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTraveller {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


}
