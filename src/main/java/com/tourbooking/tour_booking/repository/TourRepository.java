package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, String> {
}