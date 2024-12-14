package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ItineraryRepository extends JpaRepository<Itinerary, String> {
    @Modifying
    @Query("DELETE FROM Itinerary i WHERE i.tour.id = :tourId")
    void deleteAllByTourId(String tourId);
}