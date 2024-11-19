package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Payment;
import com.tourbooking.tour_booking.entity.Traveler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class PaymentToBillConverter {

    public Bill convertPaymentToBill(Payment payment) {
        Bill bill = new Bill();

        bill.setFullName(payment.getFullName());
        bill.setSpecialRequirement(payment.getSpecialRequirement());
        bill.setOthers(payment.getOthers());
        bill.setEmail(payment.getEmail());
        bill.setPhone(payment.getPhone());
        bill.setBirthDate(payment.getBirthDate());
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


        if (!payment.getTravelers().isEmpty()) {
            bill.setTraveler(payment.getTravelers().get(0));
        }

        bill.setTour(payment.getTour());
        bill.setPromotion(payment.getPromotion());



        return bill;
    }
}