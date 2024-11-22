package com.tourbooking.tour_booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationAdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
