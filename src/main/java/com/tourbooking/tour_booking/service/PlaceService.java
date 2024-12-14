package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.entity.Place;
import com.tourbooking.tour_booking.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    public List<Place> getPlaces() {
        return placeRepository.findAll();
    }
}
