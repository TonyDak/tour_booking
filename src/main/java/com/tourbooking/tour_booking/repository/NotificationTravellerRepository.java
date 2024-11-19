package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.NotificationTraveller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationTravellerRepository extends JpaRepository<NotificationTraveller, String> {
    List<NotificationTraveller> findByBillTravelerIdAndIsReadOrderByCreatedAtDesc(String travelerId, boolean isRead);
}