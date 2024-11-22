package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

}

