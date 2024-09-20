package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, String> {
}