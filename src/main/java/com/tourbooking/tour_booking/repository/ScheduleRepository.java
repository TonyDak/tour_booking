package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
}