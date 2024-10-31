package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.booking.BookingRequest;
import com.tourbooking.tour_booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/payment")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiResponse<BookingRequest> book(@RequestBody BookingRequest bookingRequest) {
        ApiResponse<BookingRequest> response = new ApiResponse<>();
        response.setMessage("Booking successfully");
        response.setResult(bookingService.book(bookingRequest));
        return response;
    }
}
