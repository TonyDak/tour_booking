package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.payment.VNPayResponse;
import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Payment;
import com.tourbooking.tour_booking.entity.Tour;
import com.tourbooking.tour_booking.service.BillService;
import com.tourbooking.tour_booking.service.PaymentService;
import com.tourbooking.tour_booking.service.TourService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final BillService billService;
    private final TourService tourService;

    @GetMapping("/tour-detail")
    public ResponseEntity<Tour> getTourDetail(@RequestParam String tourId) {
        Tour tour = tourService.getTourById(tourId);
        return ResponseEntity.ok(tour);
    }

    @PostMapping("/tour-detail")
    public String processTourDetail(@Valid @ModelAttribute("payment") Payment payment, BindingResult result) {
        if (result.hasErrors()) {
            return "tour-detail";
        }

        Payment savedPayment = paymentService.saveInitialPayment(payment);
        return "redirect:/booking/payment/" + savedPayment.getId();
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<Payment> getPaymentForm(@PathVariable String paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/prepare")
    public ResponseEntity<Bill> preparePayment(@RequestParam String billId) {
        Bill updatedBill = billService.updateBillStatus(billId, Bill.BillStatus.IN_PROGRESS, null);
        return ResponseEntity.ok(updatedBill);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Bill> confirmPayment(@RequestParam String billId) {
        Bill updatedBill = billService.updateBillStatus(billId, Bill.BillStatus.WAIT, null);
        return ResponseEntity.ok(updatedBill);
    }

    @PostMapping("/cancel")
    public ResponseEntity<Bill> cancelPayment(@RequestParam String billId, @RequestParam String cancelReason) {
        Bill updatedBill = billService.updateBillStatus(billId, Bill.BillStatus.CANCELLED, cancelReason);
        return ResponseEntity.ok(updatedBill);
    }
    @PostMapping("/payment")
    public String processPayment(@Valid @ModelAttribute("payment") Payment payment, BindingResult result) {
        if (result.hasErrors()) {
            return "payment-form";
        }

        Payment updatedPayment = paymentService.updatePayment(payment);
        Bill draftBill = billService.createBillFromPayment(updatedPayment);
        return "redirect:/booking/payment/information/" + draftBill.getId();
    }


    @GetMapping("/vn-pay")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiResponse<VNPayResponse> pay(HttpServletRequest request) {
        String random = String.valueOf((int) (Math.random() * 1000000));
        ApiResponse<VNPayResponse> response = new ApiResponse<>();
        response.setMessage("Payment created");
        response.setResult(paymentService.createVnPayPayment(request, random, "1000000"));
        return response;
    }
    @GetMapping("/vn-pay-callback")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiResponse<VNPayResponse> payCallbackHandler(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        ApiResponse<VNPayResponse> response = new ApiResponse<>();
        response.setMessage("Payment callback");
        response.setResult(paymentService.verifyVNPayTransaction(request));
        return response;
    }

}
