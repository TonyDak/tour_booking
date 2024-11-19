package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Payment;
import com.tourbooking.tour_booking.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/create")
    public ResponseEntity<Bill> createBill(@RequestBody Payment payment) {
        Bill createdBill = billService.createBillFromPayment(payment);
        return ResponseEntity.ok(createdBill);
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