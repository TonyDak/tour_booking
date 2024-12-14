package com.tourbooking.tour_booking.dto.payment;

import lombok.Builder;

@Builder
public class VNPayResponse {
    public String code;
    public String message;
    public String paymentUrl;
}
