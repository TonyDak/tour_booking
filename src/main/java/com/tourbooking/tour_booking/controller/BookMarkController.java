package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/bookmarks")
@RequiredArgsConstructor
public class BookMarkController {
    private final BookMarkService bookMarkService;


//    Thêm/xóa tour vào bookmark
    @PostMapping("/{tourId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> toggleBookmark(@PathVariable String tourId) {
        try {
            String message = bookMarkService.toggleBookmark(tourId);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


//    Danh sách tour đã bookmark
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Map<String, Object>>> getBookmarkedTours() {
        List<Map<String, Object>> bookmarkedTours = bookMarkService.getBookmarkedTours();
        return ResponseEntity.ok(bookmarkedTours);
    }


//    Check tour đã bookmark chưa
    @GetMapping("/check/{tourId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public boolean checkIfTourBookmarked(@PathVariable String tourId) {
        return bookMarkService.isTourBookmarked(tourId);
    }
}
