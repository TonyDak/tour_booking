package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, String> {
    //find id by code
    Optional<Promotion> findByCode(String code);
}