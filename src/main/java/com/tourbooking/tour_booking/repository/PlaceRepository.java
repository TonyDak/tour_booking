package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, String> {




}