package com.tourbooking.tour_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tourbooking.tour_booking.entity.Promotion;

public interface PromotionRepository  extends JpaRepository<Promotion, String> {
    boolean existsByCode(String code);
}
