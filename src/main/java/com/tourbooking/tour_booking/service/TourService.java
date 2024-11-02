package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.dto.placevisit.PlaceVisitCreate;
import com.tourbooking.tour_booking.entity.*;
import com.tourbooking.tour_booking.mapper.TourMapper;
import com.tourbooking.tour_booking.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.tourbooking.tour_booking.dto.tour.TourCreate;

import java.util.*;
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

    private final LocationRepository locationRepository;



    public TourCreate createTour(TourCreate tourCreate) {

        Tour tour = tourMapper.toTour(tourCreate);

        if (tourCreate.getLocationId() != null) {
            Location location = locationRepository.findById(tourCreate.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found with ID: " + tourCreate.getLocationId()));
            tour.setLocation(location);
        }

//        tour.getGalleries().forEach(gallery -> gallery.setTour(tour));

        tour.getSchedules().forEach(schedule -> schedule.setTour(tour));

        tour.getItineraries().forEach(itinerary -> {
            itinerary.setTour(tour);

            itinerary.getPlaceVisits().forEach(placeVisit -> {
                placeVisit.setItinerary(itinerary);
                if (placeVisit.getPlace() != null) {
                    Place place = placeRepository.findById(placeVisit.getPlace().getId())
                            .orElseThrow(() -> new RuntimeException("Place not found with ID: " + placeVisit.getPlace().getId()));
                    placeVisit.setPlace(place);
                }
            });

        });


        tourRepository.save(tour);


        return tourMapper.toTourCreate(tour);
    }




    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public List<Map<String, Object>> getAllTourSummaries() {
        List<Tour> tours = tourRepository.findAll();
        return tours.stream()
                .map(tour -> {
                    Map<String, Object> tourSummary = new LinkedHashMap<>();
                    tourSummary.put("title", tour.getTitle());
                    tourSummary.put("avt", tour.getAvt());
                    tourSummary.put("price", tour.getPrice());
                    if (tour.getLocation() != null) {

                        tourSummary.put("locationName", tour.getLocation().getName());
                    } else {

                        tourSummary.put("locationName", "Unknown");
                    }
                    return tourSummary;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getTourDetails(String tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found with ID: " + tourId));


        Map<String, Object> tourDetails = new LinkedHashMap<>();
        tourDetails.put("title", tour.getTitle());
        tourDetails.put("avt", tour.getAvt());
        tourDetails.put("price", tour.getPrice());
        tourDetails.put("location", tour.getLocation() != null ? tour.getLocation().getName() : null);

        // Add galleries
        List<String> galleries = tour.getGalleries().stream()
                .map(Gallery::getExtensions)
                .collect(Collectors.toList());
        tourDetails.put("galleries", galleries);

        // Add schedules
        List<Map<String, Object>> schedules = tour.getSchedules().stream()
                .map(schedule -> {
                    Map<String, Object> scheduleMap = new LinkedHashMap<>();
                    scheduleMap.put("start_day", schedule.getStart_day());
                    scheduleMap.put("end_day", schedule.getEnd_day());
                    scheduleMap.put("slot_booked", schedule.getSlot_booked());
                    scheduleMap.put("max_slots", schedule.getMax_slots());
                    scheduleMap.put("is_refundable", schedule.getIs_refundable());
                    return scheduleMap;
                })
                .collect(Collectors.toList());
        tourDetails.put("schedules", schedules);


        List<Map<String, Object>> itineraries = tour.getItineraries().stream()
                .map(itinerary -> {
                    Map<String, Object> itineraryMap = new LinkedHashMap<>();
                    itineraryMap.put("title", itinerary.getTitle());
                    itineraryMap.put("day_no", itinerary.getDay_no());

                    List<Map<String, Object>> placeVisits = itinerary.getPlaceVisits().stream()
                            .map(placeVisit -> {
                                Map<String, Object> placeVisitMap = new LinkedHashMap<>();
                                placeVisitMap.put("start_time", placeVisit.getStart_time());
                                placeVisitMap.put("end_time", placeVisit.getEnd_time());
                                placeVisitMap.put("description", placeVisit.getDescription());
                                placeVisitMap.put("place", placeVisit.getPlace() != null ? placeVisit.getPlace().getName() : null);
                                return placeVisitMap;
                            })
                            .collect(Collectors.toList());
                    itineraryMap.put("placeVisits", placeVisits);

                    return itineraryMap;
                })
                .collect(Collectors.toList());
        tourDetails.put("itineraries", itineraries);

        return tourDetails;
    }

    public List<Map<String, Object>> getToursByLocation(String locationName) {
        List<Tour> tours = tourRepository.findByLocationNameContainingIgnoreCase(locationName);

        if (tours.isEmpty()) {
            return Collections.emptyList();  // Return an empty list if no tours found
        }

        return tours.stream()
                .map(tour -> {
                    Map<String, Object> tourSummary = new LinkedHashMap<>();
                    tourSummary.put("title", tour.getTitle());
                    tourSummary.put("avt", tour.getAvt());
                    tourSummary.put("price", tour.getPrice());
                    tourSummary.put("location", tour.getLocation().getName());
                    return tourSummary;
                })
                .collect(Collectors.toList());
    }



    public void updateAvatar(String tourId, String avatarUrl) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found with ID: " + tourId));

        tour.setAvt(avatarUrl);
        tourRepository.save(tour);
    }

    public void updateGallery(String tourId, List<String> galleryUrls) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found with ID: " + tourId));

        List<Gallery> galleries = galleryUrls.stream()
                .map(url -> {
                    Gallery gallery = new Gallery();
                    gallery.setExtensions(url);
                    gallery.setTour(tour);
                    return gallery;
                })
                .collect(Collectors.toList());

        galleryRepository.saveAll(galleries);
    }

    public void deleteTour(String tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found with ID: " + tourId));
        tourRepository.delete(tour);
    }



}
