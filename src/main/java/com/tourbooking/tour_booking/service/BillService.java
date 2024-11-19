package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Payment;
import com.tourbooking.tour_booking.entity.Traveler;
import com.tourbooking.tour_booking.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private NotificationTravellerService notificationTravellerService;


    @Autowired
    private PaymentToBillConverter paymentToBillConverter;

    @Transactional
    public Bill createBillFromPayment(Payment payment) {
        Bill bill = paymentToBillConverter.convertPaymentToBill(payment);

        Bill savedBill = billRepository.save(bill);

        Traveler traveler = savedBill.getTraveler();
        if (traveler != null && traveler.getId() != null) {
            String travelerId = traveler.getId();
            String message = "Your booking has been successfully created. Bill ID: " + savedBill.getId();
            notificationTravellerService.createAndSendNotification(message, savedBill);
        }

        return savedBill;
    }


    public Bill getBillById(String id) {
        Optional<Bill> bill = billRepository.findById(id);
        return bill.orElse(null);
    }

    @Transactional
    public Bill updateBill(String id, Bill updatedBill) {
        Optional<Bill> existingBill = billRepository.findById(id);
        if (existingBill.isPresent()) {
            Bill bill = existingBill.get();

            // Cập nhật thông tin Bill
            bill.setFullName(updatedBill.getFullName());
            bill.setSpecialRequirement(updatedBill.getSpecialRequirement());
            bill.setOthers(updatedBill.getOthers());
            bill.setEmail(updatedBill.getEmail());
            bill.setPhone(updatedBill.getPhone());
            bill.setBirthDate(updatedBill.getBirthDate());
            bill.setGender(updatedBill.getGender());
            bill.setTotalDiscount(updatedBill.getTotalDiscount());
            bill.setTotalPrice(updatedBill.getTotalPrice());
            bill.setTotalDiscountPercent(updatedBill.getTotalDiscountPercent());
            bill.setStartDate(updatedBill.getStartDate());
            bill.setEndDate(updatedBill.getEndDate());
            bill.setTotalAdults(updatedBill.getTotalAdults());
            bill.setTotalChildren(updatedBill.getTotalChildren());
            bill.setOriginalPrice(updatedBill.getOriginalPrice());
            bill.setLocation(updatedBill.getLocation());
            bill.setAdultPrice(updatedBill.getAdultPrice());
            bill.setChildPrice(updatedBill.getChildPrice());
            bill.setUser(updatedBill.getUser());
            bill.setTour(updatedBill.getTour());
            bill.setTraveler(updatedBill.getTraveler());
            bill.setPromotion(updatedBill.getPromotionId());
            bill.setStatus(updatedBill.getStatus());
            bill.setPaymentMethod(updatedBill.getPaymentMethod());
            bill.setCancelReason(updatedBill.getCancelReason());

            return billRepository.save(bill);
        }
        return null;
    }
}
