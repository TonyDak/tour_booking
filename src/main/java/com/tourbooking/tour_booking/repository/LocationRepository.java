package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, String> {
    Optional<Location> findByName(String name);

}
