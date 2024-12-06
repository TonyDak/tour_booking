package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.payment.VNPayRequest;
import com.tourbooking.tour_booking.dto.payment.VNPayResponse;
import com.tourbooking.tour_booking.service.PaymentService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/booking")
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/payment/vn-pay")
    //@PreAuthorize("isAuthenticated()")
    public ApiResponse<VNPayResponse> pay(HttpServletRequest request,@RequestBody VNPayRequest vnPayRequest) throws ParseException {
        ApiResponse<VNPayResponse> response = new ApiResponse<>();
        response.setMessage("Payment created");
        response.setResult(paymentService.createVnPayPayment(request, vnPayRequest));
        return response;
    }
    @GetMapping("/payment/vn-pay-callback")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException, IOException {
        String vnpTxnRef = request.getParameter("vnp_TxnRef");
        paymentService.verifyVNPayTransaction(request, vnpTxnRef);
        response.sendRedirect("http://localhost:3000/booking/bill-details?id=" + vnpTxnRef);
    }
    
}
