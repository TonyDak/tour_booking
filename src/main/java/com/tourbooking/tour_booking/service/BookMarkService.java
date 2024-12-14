package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.entity.BookMark;
import com.tourbooking.tour_booking.entity.Tour;
import com.tourbooking.tour_booking.entity.User;
import com.tourbooking.tour_booking.repository.BookMarkRepository;
import com.tourbooking.tour_booking.repository.TourRepository;
import com.tourbooking.tour_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookMarkService {
    private final BookMarkRepository bookMarkRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;



    public String toggleBookmark(String tourId) {

        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));


        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new RuntimeException("Tour not found"));

        Optional<BookMark> existingBookmark = bookMarkRepository.findByTourIdAndUserId(tourId, user.getId());

        if (existingBookmark.isPresent()) {

            bookMarkRepository.delete(existingBookmark.get());
            return "Tour removed from bookmark";
        } else {

            BookMark bookMark = new BookMark();
            bookMark.setTour(tour);
            bookMark.setUser(user);
            bookMark.setCreatedAt(LocalDate.now());

            bookMarkRepository.save(bookMark);
            return "Tour added to bookmark";
        }
    }
    public List<Map<String, Object>> getBookmarkedTours() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<BookMark> bookmarks = bookMarkRepository.findAllByUserId(user.getId());

        return bookmarks.stream()
                .map(bookmark -> {
                    Map<String, Object> tourSummary = new LinkedHashMap<>();
                    Tour tour = bookmark.getTour();
                    tourSummary.put("id", tour.getId());
                    tourSummary.put("title", tour.getTitle());
                    tourSummary.put("slug", tour.getSlug());
                    tourSummary.put("avt", tour.getAvt());
                    tourSummary.put("price", tour.getPrice());
                    tourSummary.put("location", tour.getLocation() != null ? tour.getLocation().getName() : null);
                    tourSummary.put("createdAt", bookmark.getCreatedAt());
                    return tourSummary;
                })
                .collect(Collectors.toList());
    }


    public boolean isTourBookmarked(String tourId) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return bookMarkRepository.findByTourIdAndUserId(tourId, user.getId()).isPresent();
    }



}
