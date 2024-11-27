package com.tourbooking.tour_booking.dto.booking;


import jakarta.validation.Valid;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;

@Data
@Value
@Valid
public class BookingDraftRequest {
    String tour_id;
    String start_time;
}
