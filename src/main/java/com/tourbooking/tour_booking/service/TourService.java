package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.entity.*;
import com.tourbooking.tour_booking.mapper.TourMapper;
import com.tourbooking.tour_booking.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.tourbooking.tour_booking.dto.tour.TourCreate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;

    private final TourMapper tourMapper;

    private final GalleryRepository galleryRepository;


    private final ScheduleRepository scheduleRepository;


    private final ItineraryRepository itineraryRepository;


    private final PlaceVisitsRepository placeVisitRepository;


    private final PlaceRepository placeRepository;

    public TourCreate createTour(TourCreate tourCreate) {

        Tour tour = tourMapper.toTour(tourCreate);

        if (tourCreate.getLocation() != null) {
            Location location = tourMapper.toLocation(tourCreate.getLocation());
            tour.setLocation(location);
        }


        tour.getGalleries().forEach(gallery -> gallery.setTour(tour));

        tour.getSchedules().forEach(schedule -> schedule.setTour(tour));

        tour.getItineraries().forEach(itinerary -> {
            itinerary.setTour(tour);

            itinerary.getPlaceVisits().forEach(placeVisit -> {
                placeVisit.setItinerary(itinerary);

                // Xử lý place nếu cần
                // Place place = placeRepository.findById(placeVisit.getPlaceId())
                //    .orElseThrow(() -> new RuntimeException("Place not found with ID: " + placeVisit.getPlaceId()));
                // placeVisit.setPlace(place);
            });
        });

        // Save tour và các entities liên quan
        tourRepository.save(tour);

        // Trả về DTO sau khi lưu
        return tourMapper.toTourCreate(tour);
    }


    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public List<Map<String, Object>> getAllTourSummaries() {
        List<Tour> tours = tourRepository.findAll();
        return tours.stream()
                .map(tour -> {
                    Map<String, Object> tourSummary = new HashMap<>();
                    tourSummary.put("title", tour.getTitle());
                    tourSummary.put("avt", tour.getAvt());
                    tourSummary.put("price", tour.getPrice());
                    return tourSummary;
                })
                .collect(Collectors.toList());
    }


}
