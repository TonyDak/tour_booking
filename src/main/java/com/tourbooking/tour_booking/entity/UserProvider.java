package com.tourbooking.tour_booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId; // ID của người dùng từ nhà cung cấp

    public enum Provider {
        GOOGLE,
        //    FACEBOOK,
        //    GITHUB,
        //    TWITTER,
        //    LINKEDIN,
        LOCAL;

        public String getName() {
            return this.name();
        }
    }
}
