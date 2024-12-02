package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, String> {

    //find by user and status
    @Query("SELECT b FROM Bill b WHERE b.user = :user AND b.bill_status = :status")
    Page<Bill> findByUserAndBillStatus(User user, Bill.BillStatus status, Pageable pageable);
    Page<Bill> findByUser(User user, Pageable pageable);
    //find by id by query
    @Query("SELECT b FROM Bill b WHERE b.id = :id")
    Optional<Bill> findByBillId(String id);

    @Query("SELECT b FROM Bill b WHERE b.bill_status = 'DRAFT' AND b.booked_at < :timeLimit")
    List<Bill> findDraftBillsOlderThan(LocalDateTime timeLimit);

    @Query("SELECT b FROM Bill b WHERE b.bill_status = 'PENDING' AND b.booked_at < :timeLimit")
    List<Bill> findPendingBillsOlderThan(LocalDateTime timeLimit);
}