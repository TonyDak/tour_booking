package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Tour;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, String> {

    List<Tour> findByLocationNameContainingIgnoreCase(String locationName);

    public List<Tour> findBySlugContainingIgnoreCase(String slug);

    List<Tour> findAllByLocationName(String locationName);

    boolean existsBySlug(String slug);

    Optional<Tour> findBySlug(String slug);

    List<Tour> findTop10ByOrderByIdAsc();





}