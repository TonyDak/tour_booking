package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.dto.booking.BookingDraftRequest;
import com.tourbooking.tour_booking.dto.booking.BookingDraftRespone;
import com.tourbooking.tour_booking.dto.booking.BookingRequest;
import com.tourbooking.tour_booking.dto.booking.TravelerRequest;
import com.tourbooking.tour_booking.entity.*;
import com.tourbooking.tour_booking.mapper.BillMapper;
import com.tourbooking.tour_booking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final BillMapper billMapper;
    private final TourRepository tourRepository;
    private final PromotionRepository promotionRepository;
    private final TravelerRepository travelerRepository;

    @Transactional
    public BookingDraftRespone createBooking(BookingDraftRequest bookingDraftRequest) {
        Tour tour = tourRepository.findByTourId(bookingDraftRequest.getTour_id()).orElseThrow(() -> new RuntimeException("Tour not found"));
        Bill bill = billMapper.toBill(bookingDraftRequest);
        //bill_id form SOCTRIP-random chuỗi 10 number
        bill.setId("SGUTOUR-" + (int) (Math.random() * 1000000000));
        bill.setBill_status(Bill.BillStatus.DRAFT);
        bill.setTour(tour);
        billRepository.save(bill);
        return new BookingDraftRespone(bill.getId(), tour.getId(),bookingDraftRequest.getStart_time());

    }

    public BookingRequest confirmBooking(BookingRequest bookingRequest) {
        Bill bill = billRepository.findByBillId(bookingRequest.getBill_id()).orElseThrow(() -> new RuntimeException("Bill not found"));
        User user = userRepository.findByEmail(bookingRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        Tour tour = tourRepository.findByTourId(bookingRequest.getTour_id()).orElseThrow(() -> new RuntimeException("Tour not found"));
        Promotion promotion = promotionRepository.findByCode(bookingRequest.getPromotion_code()).orElse(null);
        //check promotion
        if (promotion != null) {
            //check start_time and end_time
            if (promotion.getStart_time().isAfter(LocalDate.now()) || promotion.getEnd_time().isBefore(LocalDate.now())) {
                throw new RuntimeException("Promotion is not available");
            }
            //check stock
            if (promotion.getStock() == 0) {
                throw new RuntimeException("Promotion is out of stock");
            }
            //check active
            if (promotion.getActive() == 0) {
                throw new RuntimeException("Promotion is not active");
            }
        }
        bill.setBill_status(Bill.BillStatus.PENDING);
        bill.setSpecial_requirement(bookingRequest.getSpecial_requirement());
        bill.setOther_requirement(bookingRequest.getOther_requirement());
        bill.setUser(user);
        bill.setBooked_at(LocalDateTime.now());
        //check min_traveler
        if (bookingRequest.getTravelers().size() < tour.getMin_booking_traveller()) {
            throw new RuntimeException("Number of travelers is less than min traveler");
        }
        //check type adult or child in travelers
        var total_price = 0;
        for (TravelerRequest traveler : bookingRequest.getTravelers()) {
            if (traveler.getType().equals(Traveler.TravelerType.ADULT)) {
                total_price += tour.getPrice();
            } else {
                //-40% for child
                total_price += (int) (tour.getPrice() * 0.6);
            }
        }
        //check promotion
        if (promotion != null) {
            //check min_order
            if (total_price < promotion.getMin_order()) {
                throw new RuntimeException("Total price is less than min order");
            }
            //check discount
            if (promotion.getType() == 0) {
                total_price -= promotion.getDiscount();
            } else {
                total_price -= total_price * promotion.getDiscount() / 100;
                //check max_discount
                if (total_price * promotion.getDiscount() / 100 > promotion.getMax_discount()) {
                    total_price = total_price - promotion.getMax_discount();
                }
            }
            //decrease stock
            promotion.setStock(promotion.getStock() - 1);
            promotionRepository.save(promotion);
            bill.setPromotion(promotion);
        }
        bill.setTotal_price(total_price);
        billRepository.save(bill);

        // Set the bill_id for each traveler
        for (TravelerRequest traveler : bookingRequest.getTravelers()) {
            //check bill_id
            if (travelerRepository.existsByBill_Id((bookingRequest.getBill_id()))) {
                throw new RuntimeException("Bill_id is already exist");
            }else {
                Traveler travelerEntity = billMapper.toTraveler(traveler);
                travelerEntity.setBill(bill);
                travelerRepository.save(travelerEntity);
            }
        }
        return bookingRequest;
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void deleteOldDraftBills() {
        LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(15);
        List<Bill> oldDraftBills = billRepository.findDraftBillsOlderThan(timeLimit);
        billRepository.deleteAll(oldDraftBills);
    }


    @Scheduled(fixedRate = 60000) // Run every minute
    public void deleteOldPendingBills() {
        LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(15);
        List<Bill> oldPendingBills = billRepository.findPendingBillsOlderThan(timeLimit);
        //set status to failed
        for (Bill bill : oldPendingBills) {
            bill.setBill_status(Bill.BillStatus.FAILED);
            billRepository.save(bill);
        }
    }


}
