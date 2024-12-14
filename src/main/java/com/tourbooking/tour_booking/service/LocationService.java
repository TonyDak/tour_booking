package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.entity.Location;
import com.tourbooking.tour_booking.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;



    public List<Location> getLocations() {
        return locationRepository.findAll();
    }
}
