package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, String> {
}