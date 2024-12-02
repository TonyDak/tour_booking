package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.payment.VNPayRequest;
import com.tourbooking.tour_booking.dto.payment.VNPayResponse;
import com.tourbooking.tour_booking.service.PaymentService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/booking")
public class PaymentController {
    private final PaymentService paymentService;
    @GetMapping("/payment/vn-pay")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<VNPayResponse> pay(HttpServletRequest request,@RequestBody VNPayRequest vnPayRequest) throws ParseException {
        ApiResponse<VNPayResponse> response = new ApiResponse<>();
        response.setMessage("Payment created");
        response.setResult(paymentService.createVnPayPayment(request, vnPayRequest));
        return response;
    }
    @GetMapping("/payment/vn-pay-callback")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<VNPayResponse> payCallbackHandler(HttpServletRequest request,@RequestBody VNPayRequest vnPayRequest) throws UnsupportedEncodingException, MessagingException {
        ApiResponse<VNPayResponse> response = new ApiResponse<>();
        response.setMessage("Payment callback");
        response.setResult(paymentService.verifyVNPayTransaction(request ,vnPayRequest.getBill_id()));
        return response;
    }
    
}
