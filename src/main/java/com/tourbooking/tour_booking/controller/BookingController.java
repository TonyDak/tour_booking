package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.booking.BookingDraftRequest;
import com.tourbooking.tour_booking.dto.booking.BookingDraftRespone;
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

    @PostMapping("/chosen-tour")
    public ApiResponse<BookingDraftRespone> createBooking(@RequestBody BookingDraftRequest bookingDraftRequest) {
        return ApiResponse.<BookingDraftRespone>builder().result(bookingService.createBooking(bookingDraftRequest)).build();
    }

    @PostMapping("/payment")
    public ApiResponse<BookingRequest> payment(@RequestBody BookingRequest bookingRequest) {
        return ApiResponse.<BookingRequest>builder().result(bookingService.confirmBooking(bookingRequest)).build();
    }
}
