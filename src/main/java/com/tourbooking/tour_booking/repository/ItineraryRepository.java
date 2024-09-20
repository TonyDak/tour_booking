package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<Itinerary, String> {
}