package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Payment;
import com.tourbooking.tour_booking.entity.Traveler;
import com.tourbooking.tour_booking.entity.Promotion;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class PaymentToBillConverter {

    public Bill convertPaymentToBill(Payment payment) {
        Bill bill = new Bill();

        // Thiết lập các thông tin khác của Bill từ Payment
        bill.setFullName(payment.getFullName());
        bill.setSpecialRequirement(payment.getSpecialRequirement());
        bill.setOthers(payment.getOthers());
        bill.setEmail(payment.getEmail());
        bill.setPhone(payment.getPhone());
        bill.setDateOfBirth(payment.getBirthDate());
        bill.setGender(payment.getGender());
        bill.setBookedAt(LocalDateTime.now());
        bill.setStatus(Bill.BillStatus.DRAFT);

        bill.setTotalDiscount(payment.getTotalDiscount());
        bill.setTotalPrice(payment.getTotalPrice());
        bill.setTotalDiscountPercent(payment.getTotalDiscountPercent());
        bill.setStartDate(Date.from(payment.getStartDate().atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        bill.setEndDate(Date.from(payment.getEndDate().atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        bill.setTotalAdults(payment.getTotalAdults());
        bill.setTotalChildren(payment.getTotalChildren());
        bill.setOriginalPrice(payment.getAdultTotalPrice() + payment.getChildTotalPrice());
        bill.setLocation(payment.getLocation());
        bill.setAdultPrice(payment.getAdultPrice());
        bill.setChildPrice(payment.getChildPrice());

        List<Traveler> travelers = payment.getTravelers();
        if (travelers != null && !travelers.isEmpty()) {
            bill.setTravelers(travelers);
        }

        bill.setTour(payment.getTour());

        List<Promotion> promotions = payment.getPromotions();
        if (promotions != null && !promotions.isEmpty()) {
            bill.setPromotions(promotions);
        }

        return bill;
    }
}

