package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.tour.TourCreate;
import com.tourbooking.tour_booking.entity.Tour;
import com.tourbooking.tour_booking.service.ImageService;
import com.tourbooking.tour_booking.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

//    Tạo tour
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TourCreate> createTour(@RequestBody TourCreate tourCreate) {
        ApiResponse<TourCreate> response = new ApiResponse<>();
        response.setMessage("Tour created successfully");
        response.setResult(tourService.createTour(tourCreate));
        return response;
    }

//    Chỉnh sua tour
    @PutMapping("/{tourId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Tour> updateTour(@PathVariable String tourId, @RequestBody TourCreate tourCreate) {
        ApiResponse<Tour> response = new ApiResponse<>();
        response.setMessage("Tour updated successfully");
        response.setResult(tourService.updateTour(tourId, tourCreate));
        return response;
    }

//    @GetMapping
//    public ResponseEntity<List<Tour>> getAllTours() {
//        List<Tour> tours = tourService.getAllTours();
//        return ResponseEntity.ok(tours);
//    }


//   Danh sách 10 Tour nổi bật

    @GetMapping("/outstanding")
    public ResponseEntity<List<Map<String, Object>>> getOutstandingTour() {
        List<Map<String, Object>> tours = tourService.getOutstandingTour();
        return ResponseEntity.ok(tours);
    }


//    Tất cả tour
    @GetMapping("/summaries")
    public ResponseEntity<List<Map<String, Object>>> getTourSummaries() {
        List<Map<String, Object>> summaries = tourService.getAllTourSummaries();
        return ResponseEntity.ok(summaries);
    }

//    Chi tiết tour
    @GetMapping("/details/{tourId}")
    public ResponseEntity<Map<String, Object>> getTourDetails(@PathVariable String tourId) {
        Map<String, Object> tourDetails = tourService.getTourDetails(tourId);
        return ResponseEntity.ok(tourDetails);
    }


// Lấy tat ca tour theo tên Location
@GetMapping("/location/{locationName}")
public ResponseEntity<Map<String, Object>> getToursByLocation(@PathVariable String locationName) {
    Map<String, Object> locationWithTours = tourService.getToursByLocationName(locationName);
    return ResponseEntity.ok(locationWithTours);
}


    // Tìm kiếm tour theo location, slug, giá
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchTours(
            @RequestParam String locationOrSlug,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice) {

        List<Map<String, Object>> tours = tourService.searchTours(locationOrSlug, minPrice, maxPrice);

        if (tours.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tours);
    }



//    Lấy tour bằng slug
    @GetMapping("slug/{slug}")
    public ResponseEntity<Map<String, Object>> getTourBySlug(@PathVariable String slug) {
        Map<String, Object> tourDetails = tourService.getTourDetailsBySlug(slug);
        return ResponseEntity.ok(tourDetails);
    }

//    Cập nhật avatar
    @PostMapping("/{tourId}/upload-avatar")
    @PreAuthorize("hasRole('ADMIN')")
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

//    Cập nhật gallery
    @PostMapping("/{tourId}/upload-gallery")
    @PreAuthorize("hasRole('ADMIN')")
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

//    Xóa
@DeleteMapping("/{tourId}")
public ResponseEntity<String> deleteTour(@PathVariable String tourId) {
    try {
        tourService.deleteTour(tourId);
        return ResponseEntity.ok("Tour with ID " + tourId + " has been deleted.");
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}





}
