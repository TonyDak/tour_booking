package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelerRepository extends JpaRepository<Traveler, String> {
}