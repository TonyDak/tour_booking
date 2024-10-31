package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.tour.TourCreate;
import com.tourbooking.tour_booking.entity.Tour;
import com.tourbooking.tour_booking.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/tours")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;


    @PostMapping
    public ApiResponse<TourCreate> createTour(@RequestBody TourCreate tourCreate) {
        ApiResponse<TourCreate> response = new ApiResponse<>();
        response.setMessage("Tour created successfully");
        response.setResult(tourService.createTour(tourCreate));
        return response;
    }
//
    @GetMapping
    public ResponseEntity<List<Tour>> getAllTours() {
        List<Tour> tours = tourService.getAllTours();
        return ResponseEntity.ok(tours);
    }

    @GetMapping("/summaries")
    public ResponseEntity<List<Map<String, Object>>> getTourSummaries() {
        List<Map<String, Object>> summaries = tourService.getAllTourSummaries();
        return ResponseEntity.ok(summaries);
    }


}
