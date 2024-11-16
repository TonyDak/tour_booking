package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.payment.VNPayResponse;
import com.tourbooking.tour_booking.service.PaymentService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;
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
