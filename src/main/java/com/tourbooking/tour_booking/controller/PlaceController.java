package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.entity.Place;
import com.tourbooking.tour_booking.entity.PlaceVisits;
import com.tourbooking.tour_booking.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = placeService.getPlaces();
        return ResponseEntity.ok(places);
    }







}
