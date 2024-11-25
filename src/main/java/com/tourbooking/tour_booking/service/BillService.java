package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Payment;
import com.tourbooking.tour_booking.entity.Promotion;
import com.tourbooking.tour_booking.entity.Traveler;
import com.tourbooking.tour_booking.repository.BillRepository;
import com.tourbooking.tour_booking.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private NotificationTravellerService notificationTravellerService;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PaymentToBillConverter paymentToBillConverter;

    @Transactional
    public Bill createBillFromPayment(Payment payment) {
        Bill bill = paymentToBillConverter.convertPaymentToBill(payment);

        Bill savedBill = billRepository.save(bill);

        List<Traveler> travelers = savedBill.getTravelers();
        if (travelers != null && !travelers.isEmpty()) {
            Traveler traveler = travelers.get(0);

            // Kiểm tra và gửi thông báo cho traveler
            if (traveler != null && traveler.getId() != null) {
                String travelerId = traveler.getId();
                String message = "Your booking has been successfully created. Bill ID: " + savedBill.getId();
                notificationTravellerService.createAndSendNotification(message, savedBill);
            }
        }

        return savedBill;
    }


    @Transactional
    public Bill updateBillStatus(String billId, Bill.BillStatus newStatus, String cancelReason) {
        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new RuntimeException("Bill not found with ID: " + billId);
        }

        bill.setStatus(newStatus);
        if (newStatus == Bill.BillStatus.CANCELLED) {
            if (cancelReason == null || cancelReason.trim().isEmpty()) {
                throw new IllegalArgumentException("Cancel reason is required for cancellation");
            }
            bill.setCancelReason(cancelReason);
        }

        return billRepository.save(bill);
    }
    @Transactional
    public Bill addPromotionToBill(String billId, String promotionCode) {
        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new RuntimeException("Bill not found with ID: " + billId);
        }

        Optional<Promotion> promotionOpt = promotionRepository.findByCode(promotionCode);
        if (promotionOpt.isEmpty()) {
            throw new RuntimeException("Promotion not found with code: " + promotionCode);
        }

        Promotion promotion = promotionOpt.get();
        bill.addPromotion(promotion);
        return billRepository.save(bill);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
    @Transactional
    public Bill removePromotionFromBill(String billId, String promotionId) {

        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new RuntimeException("Bill not found with ID: " + billId);
        }

        Optional<Promotion> promotionOpt = promotionRepository.findById(promotionId);
        if (promotionOpt.isEmpty()) {
            throw new RuntimeException("Promotion not found with ID: " + promotionId);
        }

        Promotion promotion = promotionOpt.get();
        bill.removePromotion(promotion);
        return billRepository.save(bill);
    }


    @Transactional
    public Bill updateBillWithTravelerInfo(String billId, Map<String, String> formData) {
        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new RuntimeException("Bill not found with ID: " + billId);
        }

        // Cập nhật thông tin của Bill
        bill.setFullName(formData.get("fullName"));
        bill.setEmail(formData.get("email"));
        bill.setPhone(formData.get("phone"));
        bill.setSpecialRequirement(formData.get("specialRequirement"));
        bill.setOthers(formData.get("others"));

        // Cập nhật ngày sinh nếu có
        String dateOfBirthStr = formData.get("dateOfBirth");
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr, DateTimeFormatter.ISO_DATE);
            bill.setDateOfBirth(dateOfBirth);
        }

        // Cập nhật giới tính nếu có
        String gender = formData.get("gender");
        if (gender != null && !gender.isEmpty()) {
            bill.setGender(gender);
        }

        // Cập nhật trạng thái Bill
        bill.setStatus(Bill.BillStatus.DRAFT);

        // Cập nhật thông tin Traveler (người lớn và trẻ em)
        List<Traveler> travelers = bill.getTravelers();
        if (travelers == null) {
            travelers = new ArrayList<>();
            bill.setTravelers(travelers);
        }

        // Xử lý người lớn
        String adultName = formData.get("adultName");
        if (adultName != null && !adultName.isEmpty()) {
            Traveler adult = new Traveler();
            adult.setName(adultName);
            adult.setEmail(formData.get("adultEmail"));
            adult.setPhoneNumber(formData.get("adultPhone"));
            adult.setDateOfBirth(LocalDate.parse(formData.get("adultDateOfBirth"), DateTimeFormatter.ISO_DATE));
            adult.setGender(formData.get("adultGender"));
            adult.setType(1); // Người lớn
            adult.setBill(bill);
            travelers.add(adult);
        }

        // Xử lý trẻ em
        String childrenNames = formData.get("childrenNames"); // Dữ liệu có thể là danh sách tên trẻ em
        if (childrenNames != null && !childrenNames.isEmpty()) {
            String[] childrenNameArray = childrenNames.split(","); // Giả sử tên trẻ em được phân tách bởi dấu phẩy
            for (String childName : childrenNameArray) {
                Traveler child = new Traveler();
                child.setName(childName.trim());
                child.setDateOfBirth(LocalDate.parse(formData.get("childDateOfBirth"), DateTimeFormatter.ISO_DATE));
                child.setGender(formData.get("childGender"));
                child.setType(2); // Trẻ em
                child.setBill(bill);
                travelers.add(child);
            }
        }

        // Lưu Bill với các thông tin đã cập nhật
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

        List<Traveler> travelers = new ArrayList<>();
        bill.setTravelers(travelers);

        List<Promotion> promotions = new ArrayList<>();
        bill.setPromotions(promotions);

        if (payment.getAdults() != null && !payment.getAdults().isEmpty()) {
            for (Traveler adultTraveler : payment.getAdults()) {
                adultTraveler.setBill(bill);
                travelers.add(adultTraveler);
            }
        }

        if (payment.getChildren() != null && !payment.getChildren().isEmpty()) {
            for (Traveler childTraveler : payment.getChildren()) {
                childTraveler.setBill(bill);
                travelers.add(childTraveler);
            }
        }

        bill.setTravelers(travelers);

        if (payment.getPromotions() != null && !payment.getPromotions().isEmpty()) {
            promotions.addAll(payment.getPromotions());
            bill.setPromotions(promotions);
        }

        // Save and return the bill
        return billRepository.save(bill);
    }

    public Bill getBillById(String id) {
        Optional<Bill> bill = billRepository.findById(id);
        return bill.orElse(null);
    }

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
            bill.setStatus(updatedBill.getStatus());
            bill.setPaymentMethod(updatedBill.getPaymentMethod());
            bill.setCancelReason(updatedBill.getCancelReason());

            // Cập nhật danh sách Traveler
            // Xóa tất cả các traveler cũ và thêm những traveler mới từ updatedBill
            Set<Traveler> updatedTravelers = new HashSet<>(updatedBill.getTravelers());
            bill.getTravelers().clear();
            bill.getTravelers().addAll(updatedTravelers);

            // Cập nhật danh sách Promotion
            Set<Promotion> updatedPromotions = new HashSet<>(updatedBill.getPromotions());
            bill.getPromotions().clear();
            bill.getPromotions().addAll(updatedPromotions);

            // Lưu bill đã cập nhật
            return billRepository.save(bill);
        }
        return null;
    }

}
