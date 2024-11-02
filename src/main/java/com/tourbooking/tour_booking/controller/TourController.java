package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.tour.TourCreate;
import com.tourbooking.tour_booking.entity.Tour;
import com.tourbooking.tour_booking.service.ImageService;
import com.tourbooking.tour_booking.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/tours")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;

    private final ImageService imageService;

    @PostMapping
    public ApiResponse<TourCreate> createTour(@RequestBody TourCreate tourCreate) {
        ApiResponse<TourCreate> response = new ApiResponse<>();
        response.setMessage("Tour created successfully");
        response.setResult(tourService.createTour(tourCreate));
        return response;
    }

//    @GetMapping
//    public ResponseEntity<List<Tour>> getAllTours() {
//        List<Tour> tours = tourService.getAllTours();
//        return ResponseEntity.ok(tours);
//    }

    @GetMapping("/summaries")
    public ResponseEntity<List<Map<String, Object>>> getTourSummaries() {
        List<Map<String, Object>> summaries = tourService.getAllTourSummaries();
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/datails/{tourId}")
    public ResponseEntity<Map<String, Object>> getTourDetails(@PathVariable String tourId) {
        Map<String, Object> tourDetails = tourService.getTourDetails(tourId);
        return ResponseEntity.ok(tourDetails);
    }



    @GetMapping("/search")
    public ResponseEntity<Object> searchToursByLocation(@RequestParam String location) {
        List<Map<String, Object>> tours = tourService.getToursByLocation(location);

        if (tours.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No tours found for location: " + location);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(tours);
    }

    @PostMapping("/{tourId}/upload-avatar")
    public ResponseEntity<Map<String, String>> uploadAvatar(
            @PathVariable String tourId,
            @RequestParam("file") MultipartFile file) {


        String avatarUrl = imageService.uploadImage(file, "avt");


        tourService.updateAvatar(tourId, avatarUrl);

        Map<String, String> response = new HashMap<>();
        response.put("tourId", tourId);
        response.put("avatarUrl", avatarUrl);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{tourId}/upload-gallery")
    public ResponseEntity<Map<String, List<String>>> uploadGallery(
            @PathVariable String tourId,
            @RequestParam("files") List<MultipartFile> files) {

        List<String> galleryUrls = files.stream()
                .map(file -> imageService.uploadImage(file, "gallery"))
                .toList();

        tourService.updateGallery(tourId, galleryUrls);

        Map<String, List<String>> response = new HashMap<>();
        response.put("tourId", List.of(tourId));
        response.put("galleryUrls", galleryUrls);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{tourId}")
    public ResponseEntity<Map<String, String>> deleteTour(@PathVariable String tourId) {
        tourService.deleteTour(tourId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tour deleted successfully");
        response.put("tourId", tourId);

        return ResponseEntity.ok(response);
    }






}
