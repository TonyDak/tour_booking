package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.BookMark;
import com.tourbooking.tour_booking.entity.Tour;
import com.tourbooking.tour_booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, String> {
    Optional<BookMark> findByTourIdAndUserId(String tourId, String userId);
}