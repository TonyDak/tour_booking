package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.PlaceVisits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceVisitsRepository extends JpaRepository<PlaceVisits, String> {
}