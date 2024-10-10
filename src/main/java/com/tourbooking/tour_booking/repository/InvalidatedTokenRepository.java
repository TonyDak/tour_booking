package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}