package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.dto.placevisit.PlaceVisitCreate;
import com.tourbooking.tour_booking.entity.*;
import com.tourbooking.tour_booking.mapper.TourMapper;
import com.tourbooking.tour_booking.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.tourbooking.tour_booking.dto.tour.TourCreate;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
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

        String baseSlug = toSlug(tourCreate.getTitle());
        String uniqueSlug = baseSlug;
        int counter = 1;

        while (tourRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        }

        tour.setSlug(uniqueSlug);

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

    @Transactional
    public Tour updateTour(String tourId, TourCreate tourCreate) {

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found with ID: " + tourId));

        tour.setTitle(tourCreate.getTitle());
        tour.setPrice(tourCreate.getPrice());
        tour.setHighlight(tourCreate.getHighlight());
        tour.setMin_booking_traveller(Integer.parseInt(tourCreate.getMin_booking_traveller()));
        tour.setPolicy(tourCreate.getPolicy());
        tour.setTotal_days(Integer.parseInt(tourCreate.getTotal_days()));


        if (tourCreate.getLocationId() != null) {
            Location location = locationRepository.findById(tourCreate.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found with ID: " + tourCreate.getLocationId()));
            tour.setLocation(location);
        }
        String baseSlug = toSlug(tourCreate.getTitle());


        tour.setSlug(baseSlug);


        scheduleRepository.deleteAllByTourId(tourId);
        placeVisitRepository.deleteAllByTourId(tourId);
        itineraryRepository.deleteAllByTourId(tourId);




        List<Schedule> newSchedules = tourCreate.getSchedules().stream()
                .map(scheduleCreate -> {
                    Schedule schedule = tourMapper.toSchedule(scheduleCreate);
                    schedule.setTour(tour);
                    return schedule;
                }).collect(Collectors.toList());
        scheduleRepository.saveAll(newSchedules);



        // Xử lý các itineraries mới
        List<Itinerary> newItineraries = tourCreate.getItineraries().stream()
                .map(itineraryCreate -> {
                    Itinerary itinerary = tourMapper.toItinerary(itineraryCreate);
                    itinerary.setTour(tour);

                    // Xử lý các placeVisits trong mỗi itinerary
                    itinerary.getPlaceVisits().forEach(placeVisit -> {
                        placeVisit.setItinerary(itinerary);
                        if (placeVisit.getPlace() != null) {
                            // Tìm kiếm Place và gán lại cho placeVisit
                            Place place = placeRepository.findById(placeVisit.getPlace().getId())
                                    .orElseThrow(() -> new RuntimeException("Place not found with ID: " + placeVisit.getPlace().getId()));
                            placeVisit.setPlace(place);
                        }
                    });

                    return itinerary;
                }).collect(Collectors.toList());
        itineraryRepository.saveAll(newItineraries);

        // Lưu tour sau khi cập nhật
        tourRepository.save(tour);

        return tour;

    }





    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }


    public List<Map<String, Object>> getOutstandingTour() {
        List<Tour> tours = tourRepository.findTop10ByOrderByIdAsc();

        return tours.stream()
                .map(tour -> {
                    Map<String, Object> tourSummary = new LinkedHashMap<>();
                    tourSummary.put("id", tour.getId());
                    tourSummary.put("slug", tour.getSlug());
                    tourSummary.put("title", tour.getTitle());
                    tourSummary.put("avt", tour.getAvt());
                    tourSummary.put("price", tour.getPrice());
                    tourSummary.put("location", tour.getLocation() != null ? tour.getLocation().getName() : null);
                    return tourSummary;
                })
                .collect(Collectors.toList());
    }


    public List<Map<String, Object>> getAllTourSummaries() {
        List<Tour> tours = tourRepository.findAll();
        return tours.stream()
                .map(tour -> {
                    Map<String, Object> tourSummary = new LinkedHashMap<>();
                    tourSummary.put("id", tour.getId());
                    tourSummary.put("title", tour.getTitle());
                    tourSummary.put("slug", tour.getSlug());
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
        tourDetails.put("id", tour.getId());
        tourDetails.put("title", tour.getTitle());
        tourDetails.put("slug", tour.getSlug());
        tourDetails.put("avt", tour.getAvt());
        tourDetails.put("price", tour.getPrice());
        tourDetails.put("location", tour.getLocation() != null ? tour.getLocation().getName() : null);
        tourDetails.put("total_days", tour.getTotal_days());
        tourDetails.put("start_days", tour.getStart_days());
        tourDetails.put("end_days", tour.getEnd_days());
        tourDetails.put("bookable_start_date", tour.getBookable_start_date());
        tourDetails.put("bookable_end_date", tour.getBookable_end_date());
        tourDetails.put("policy", tour.getPolicy());
        tourDetails.put("min_booking_traveller", tour.getMin_booking_traveller());

        // Add galleries
        List<String> galleries = tour.getGalleries().stream()
                .map(Gallery::getPath)
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

    public List<Map<String, Object>> searchTours(String locationOrSlug, Long minPrice, Long maxPrice) {

        String searchSlug = toSlug(locationOrSlug); // Ví dụ: Hà Nội -> ha-noi

        List<Tour> toursByLocation = tourRepository.findByLocationNameContainingIgnoreCase(locationOrSlug);

        List<Tour> toursBySlug = tourRepository.findBySlugContainingIgnoreCase(searchSlug);

        Set<Tour> tours = new HashSet<>();
        tours.addAll(toursByLocation);
        tours.addAll(toursBySlug);

        if (minPrice != null && maxPrice != null) {
            tours = tours.stream()
                    .filter(t -> t.getPrice() >= minPrice && t.getPrice() <= maxPrice)
                    .collect(Collectors.toSet());
        }

        if (tours.isEmpty()) {
            return Collections.emptyList();
        }


        return tours.stream()
                .map(tour -> {
                    Map<String, Object> tourSummary = new LinkedHashMap<>();
                    tourSummary.put("id", tour.getId());
                    tourSummary.put("slug", tour.getSlug());
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

    @Transactional
    public void updateGallery(String tourId, List<String> galleryUrls) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found with ID: " + tourId));

        galleryRepository.deleteAllByTourId(tourId);

        List<Gallery> galleries = galleryUrls.stream()
                .map(url -> {
                    Gallery gallery = new Gallery();
                    gallery.setPath(url);
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

    private String toSlug(String input) {
        input = input.replace("đ", "d").replace("Đ", "D");
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String noDiacritics = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return noDiacritics.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

    public Map<String, Object> getTourDetailsBySlug(String slug) {
        // Tìm tour theo slug
        Tour tour = tourRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Tour not found with slug: " + slug));

        // Tạo map chi tiết tour
        Map<String, Object> tourDetails = new LinkedHashMap<>();
        tourDetails.put("id", tour.getId());
        tourDetails.put("title", tour.getTitle());
        tourDetails.put("slug", tour.getSlug());
        tourDetails.put("avt", tour.getAvt());
        tourDetails.put("price", tour.getPrice());
        tourDetails.put("location", tour.getLocation() != null ? tour.getLocation().getName() : null);
        tourDetails.put("total_days", tour.getTotal_days());
        tourDetails.put("start_days", tour.getStart_days());
        tourDetails.put("end_days", tour.getEnd_days());
        tourDetails.put("bookable_start_date", tour.getBookable_start_date());
        tourDetails.put("bookable_end_date", tour.getBookable_end_date());
        tourDetails.put("policy", tour.getPolicy());
        tourDetails.put("min_booking_traveller", tour.getMin_booking_traveller());

        // Thêm galleries
        List<String> galleries = tour.getGalleries().stream()
                .map(Gallery::getPath)
                .collect(Collectors.toList());
        tourDetails.put("galleries", galleries);

        // Thêm schedules
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

        // Thêm itineraries
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











}
