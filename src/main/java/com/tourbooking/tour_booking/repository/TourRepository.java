package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Tour;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, String> {

    List<Tour> findByTitleContaining(String title);

    List<Tour> findByPriceBetween(Double min, Double max);

    List<Tour> findByUserId(String userId);

}