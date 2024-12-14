package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.PlaceVisits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PlaceVisitsRepository extends JpaRepository<PlaceVisits, String> {
    @Modifying
    @Query("DELETE FROM PlaceVisits p WHERE p.itinerary.tour.id = :tourId")
    void deleteAllByTourId(String tourId);
}