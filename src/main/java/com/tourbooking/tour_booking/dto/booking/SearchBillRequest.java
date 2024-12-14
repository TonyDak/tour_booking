package com.tourbooking.tour_booking.dto.booking;

import lombok.Data;

@Data
public class SearchBillRequest {
    String bill_id;
    String pin_code;
}
