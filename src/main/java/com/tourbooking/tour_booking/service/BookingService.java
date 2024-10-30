package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.dto.booking.BookingRequest;
import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Promotion;
import com.tourbooking.tour_booking.entity.Tour;
import com.tourbooking.tour_booking.entity.User;
import com.tourbooking.tour_booking.mapper.BillMapper;
import com.tourbooking.tour_booking.repository.BillRepository;
import com.tourbooking.tour_booking.repository.PromotionRepository;
import com.tourbooking.tour_booking.repository.TourRepository;
import com.tourbooking.tour_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final BillMapper billMapper;
    private final TourRepository tourRepository;
    private final PromotionRepository promotionRepository;

    public BookingRequest book(BookingRequest bookingRequest) {
        Bill bill = billMapper.toBill(bookingRequest);

        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        bill.setUser(user);

        Tour tour = tourRepository.findById(bookingRequest.getTour_id()).orElseThrow(() -> new RuntimeException("Tour not found"));
        bill.setTour(tour);

        Promotion promotion = promotionRepository.findByCode(bookingRequest.getPromotion_code()).orElseThrow(() -> new RuntimeException("Promotion not found"));
        bill.setPromotion(promotion);
        billRepository.save(bill);
        return bookingRequest;
    }
}
