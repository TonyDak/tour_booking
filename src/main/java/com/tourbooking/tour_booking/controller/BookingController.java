package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.booking.*;
import com.tourbooking.tour_booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/chosen-tour")
    public ApiResponse<BookingDraftRespone> createBooking(@RequestBody BookingDraftRequest bookingDraftRequest) {
        return ApiResponse.<BookingDraftRespone>builder().result(bookingService.createBooking(bookingDraftRequest)).build();
    }

    @GetMapping("/payment/{billId}")
    public ApiResponse<BookingDraftDetailRespone> getBooking(@PathVariable String billId) {
        return ApiResponse.<BookingDraftDetailRespone>builder().result(bookingService.getBooking(billId)).build();
    }

    @GetMapping("/payment/detail/{billId}")
    public ApiResponse<BookingDetailRespone> getBookingDetail(@PathVariable String billId) {
        return ApiResponse.<BookingDetailRespone>builder().result(bookingService.getBookingDetail(billId)).build();
    }

    @PostMapping("/payment")
    public ApiResponse<BookingRequest> payment(@RequestBody BookingRequest bookingRequest) {
        return ApiResponse.<BookingRequest>builder().result(bookingService.confirmBooking(bookingRequest)).build();
    }

    @PostMapping("/payment/search")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<BookingDetailRespone> searchBooking(@RequestBody SearchBillRequest request) {
        return ApiResponse.<BookingDetailRespone>builder().result(bookingService.getBookingDetail(request.getBill_id(), request.getPin_code())).build();
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<BookingDetailRespone>> getAllBooking() {
        return ApiResponse.<List<BookingDetailRespone>>builder().result(bookingService.getAllBooking()).build();
    }
}
