package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Payment;
import com.tourbooking.tour_booking.entity.Tour;
import com.tourbooking.tour_booking.service.BillService;
import com.tourbooking.tour_booking.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private TourService tourService;

    @GetMapping("/tour/{tourId}")
    public ResponseEntity<Tour> getTourBookingForm(@PathVariable String tourId) {
        Tour tour = tourService.getTourById(tourId);
        return ResponseEntity.ok(tour);
    }

    @PostMapping("/create")
    public ResponseEntity<Bill> createDraftBill(@RequestBody Payment payment) {
        Bill draftBill = billService.createDraftBill(payment);
        return ResponseEntity.ok(draftBill);
    }

    @GetMapping("/payment/{billId}")
    public ResponseEntity<Bill> getPaymentPage(@PathVariable String billId) {
        Bill bill = billService.getBillById(billId);
        return ResponseEntity.ok(bill);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Bill> confirmBooking(@RequestParam String billId, @RequestBody Map<String, String> formData) {
        Bill updatedBill = billService.updateBillWithTravelerInfo(billId, formData);
        return ResponseEntity.ok(updatedBill);
    }

    @GetMapping("/payment/information/{billId}")
    public ResponseEntity<Bill> getPaymentInformationPage(@PathVariable String billId) {
        Bill bill = billService.getBillById(billId);
        return ResponseEntity.ok(bill);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable String id) {
        Bill bill = billService.getBillById(id);
        if (bill != null) {
            return ResponseEntity.ok(bill);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable String id, @RequestBody Bill bill) {
        Bill updatedBill = billService.updateBill(id, bill);
        if (updatedBill != null) {
            return ResponseEntity.ok(updatedBill);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

