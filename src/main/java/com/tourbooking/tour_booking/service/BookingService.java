package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.dto.booking.*;
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
        Tour tour = tourRepository.findById(bookingDraftRequest.getTour_id()).orElseThrow(() -> new RuntimeException("Tour not found"));
        Bill bill = billMapper.toBill(bookingDraftRequest);
        //bill_id form SOCTRIP-random chuỗi 10 number
        bill.setTotal_adult(bookingDraftRequest.getAdult_quantity());
        bill.setTotal_child(bookingDraftRequest.getChildren_quantity());
        bill.setId("SGUTOUR-" + (int) (Math.random() * 1000000000));
        bill.setBill_status(Bill.BillStatus.DRAFT);
        bill.setTour(tour);
        billRepository.save(bill);
        return new BookingDraftRespone(bill.getId(), tour.getId(),bookingDraftRequest.getStart_time(),bookingDraftRequest.getAdult_quantity(),bookingDraftRequest.getChildren_quantity());

    }

    //get bill by bill_id
    public BookingDraftDetailRespone getBooking(String billId) {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new RuntimeException("Bill not found"));
        Tour tour = bill.getTour();
        if (tour == null) {
            throw new RuntimeException("Tour not found in the bill");
        }
        return new BookingDraftDetailRespone(bill);
    }

    //get bill detail by bill_id
    public BookingDetailRespone getBookingDetail(String billId) {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new RuntimeException("Bill not found"));
        Tour tour = bill.getTour();
        if (tour == null) {
            throw new RuntimeException("Tour not found in the bill");
        }
        return new BookingDetailRespone(bill);
    }

    @Transactional
    public BookingRequest confirmBooking(BookingRequest bookingRequest) {
        Bill bill = billRepository.findByBillId(bookingRequest.getBill_id()).orElseThrow(() -> new RuntimeException("Bill not found"));
        User user = userRepository.findByEmail(bookingRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        Promotion promotion = promotionRepository.findByCode(bookingRequest.getPromotion_code()).orElse(null);
        Tour tour = bill.getTour();
        if (tour == null) {
            throw new RuntimeException("Tour not found in the bill");
        }

        //check bookable_start_date and bookable_end_date
//        if (tour.getBookable_start_date().isAfter(LocalDate.now()) || tour.getBookable_end_date().isBefore(LocalDate.now())) {
//            throw new RuntimeException("Tour is not available");
//        }

        // Check promotion
        if (promotion != null) {
            if (promotion.getStart_time().isAfter(LocalDate.now()) || promotion.getEnd_time().isBefore(LocalDate.now())) {
                throw new RuntimeException("Promotion is not available");
            }
            if (promotion.getStock() == 0) {
                throw new RuntimeException("Promotion is out of stock");
            }
            if (promotion.getActive() == 0) {
                throw new RuntimeException("Promotion is not active");
            }
        }

        bill.setBill_status(Bill.BillStatus.PENDING);
        bill.setSpecial_requirement(bookingRequest.getSpecial_requirement());
        bill.setOther_requirement(bookingRequest.getOther_requirement());
        bill.setUser(user);
        bill.setBooked_at(LocalDateTime.now());

        // Check min_traveler
        if (bookingRequest.getTravelers().size() < tour.getMin_booking_traveller()) {
            throw new RuntimeException("Number of travelers is less than min traveler");
        }

        // Calculate total price
        int total_price = 0;
        total_price += (int) (tour.getPrice() * bill.getTotal_adult());
        total_price += (int) (tour.getPrice() * 0.6) * bill.getTotal_child();

        // Apply promotion
        if (promotion != null) {
            if (total_price < promotion.getMin_order()) {
                throw new RuntimeException("Total price is less than min order");
            }
            if (promotion.getType() == 0) {
                total_price -= promotion.getDiscount();
            } else {
                int discountAmount = total_price * promotion.getDiscount() / 100;
                if (discountAmount > promotion.getMax_discount()) {
                    discountAmount = promotion.getMax_discount();
                }
                total_price -= discountAmount;
            }
            promotion.setStock(promotion.getStock() - 1);
            promotionRepository.save(promotion);
            bill.setPromotion(promotion);
        }

        // Set the bill_id for each traveler
        for (TravelerRequest traveler : bookingRequest.getTravelers()) {
            if (travelerRepository.existsByBill_Id(bookingRequest.getBill_id())) {
                continue;
            } else {
                Traveler travelerEntity = billMapper.toTraveler(traveler);
                travelerEntity.setBill(bill);
                travelerRepository.save(travelerEntity);
            }
        }

        bill.setTotal_price(total_price);
        billRepository.save(bill);


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
