package com.tourbooking.tour_booking.dto.booking;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
public class BookingDraftRespone {
    String bill_id;
    String tour_id;
    String start_time;
    Integer adult_quantity;
    Integer children_quantity;
}
