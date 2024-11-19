package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.NotificationAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationAdminRepository extends JpaRepository<NotificationAdminEntity, String> {
    List<NotificationAdminEntity> findByIsReadOrderByCreatedAtDesc(boolean isRead);
}