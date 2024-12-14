package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.tour.id = :tourId")
    void deleteAllByTourId(String tourId);


}