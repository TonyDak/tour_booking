package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GalleryRepository extends JpaRepository<Gallery, String> {
    @Modifying
    @Query("DELETE FROM Gallery g WHERE g.tour.id = :tourId")
    void deleteAllByTourId(@Param("tourId") String tourId);
}