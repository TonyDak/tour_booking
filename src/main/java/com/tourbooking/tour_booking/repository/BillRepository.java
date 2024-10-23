package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, String> {

    
}