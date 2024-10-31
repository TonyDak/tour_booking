package com.tourbooking.tour_booking.dto.schedule;

import jakarta.validation.Valid;
import lombok.Value;
import com.tourbooking.tour_booking.entity.Schedule;

import java.time.LocalDate;

/**
 * DTO for {@link Schedule}
 */

@Valid
@Value
public class ScheduleCreate {
    String start_day;
    String end_day;
    Integer slot_booked;
    Integer max_slots;
    Boolean is_refundable;
}
