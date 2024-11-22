package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Payment;
import com.tourbooking.tour_booking.entity.Traveler;
import com.tourbooking.tour_booking.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
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


    @Transactional
    public Bill updateBillWithTravelerInfo(String billId, Map<String, String> formData) {
        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new RuntimeException("Bill not found with ID: " + billId);
        }

        // Update bill with traveler information
        bill.setFullName(formData.get("fullName"));
        bill.setEmail(formData.get("email"));
        bill.setPhone(formData.get("phone"));
        bill.setSpecialRequirement(formData.get("specialRequirement"));
        bill.setOthers(formData.get("others"));

        // Update date of birth if provided
        String dateOfBirthStr = formData.get("dateOfBirth");
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr, DateTimeFormatter.ISO_DATE);
            bill.setDateOfBirth(dateOfBirth);
        }

        // Update gender if provided
        String gender = formData.get("gender");
        if (gender != null && !gender.isEmpty()) {
            bill.setGender(gender);
        }

        // Update traveler information
        Traveler traveler = bill.getTraveler();
        if (traveler == null) {
            traveler = new Traveler();
            bill.setTraveler(traveler);
        }
        traveler.setName(bill.getFullName());
        traveler.setEmail(bill.getEmail());
        traveler.setPhoneNumber(bill.getPhone());
        traveler.setDateOfBirth(bill.getDateOfBirth());
        traveler.setGender(bill.getGender());

        // Update bill status
        bill.setStatus(Bill.BillStatus.DRAFT);

        // Save the updated bill
        return billRepository.save(bill);
    }

    public Bill createDraftBill(Payment payment) {
        Bill bill = new Bill();

        bill.setFullName(payment.getFullName());
        bill.setEmail(payment.getEmail());
        bill.setPhone(payment.getPhone());
        bill.setSpecialRequirement(payment.getSpecialRequirement());
        bill.setOthers(payment.getOthers());
        bill.setLocation(payment.getLocation());

        bill.setTour(payment.getTour());
        bill.setStartDate(new Date(payment.getStartDate().toEpochDay()));
        bill.setEndDate(new Date(payment.getEndDate().toEpochDay()));


        bill.setTotalDiscount(payment.getTotalDiscount());
        bill.setTotalPrice(payment.getTotalPrice());
        bill.setTotalDiscountPercent(payment.getTotalDiscountPercent());
        bill.setAdultPrice(payment.getAdultPrice());
        bill.setChildPrice(payment.getChildPrice());
        bill.setTotalAdults(payment.getTotalAdults());
        bill.setTotalChildren(payment.getTotalChildren());
        bill.setOriginalPrice(payment.getAdultTotalPrice() + payment.getChildTotalPrice());

        bill.setStatus(Bill.BillStatus.DRAFT);
        bill.setBookedAt(LocalDateTime.now());
        bill.setCreatedAt(LocalDateTime.now());
        bill.setUpdatedAt(LocalDateTime.now());


        if (payment.getPromotion() != null) {
            bill.setPromotion(payment.getPromotion());
        }

        if (!payment.getTravelers().isEmpty()) {
            Traveler mainTraveler = payment.getTravelers().get(0);
            bill.setTraveler(mainTraveler);
            bill.setDateOfBirth(mainTraveler.getDateOfBirth());
            bill.setGender(mainTraveler.getGender());
        }


        return billRepository.save(bill);
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
            bill.setDateOfBirth(updatedBill.getDateOfBirth());
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
