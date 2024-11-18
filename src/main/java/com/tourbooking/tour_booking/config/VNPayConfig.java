package com.tourbooking.tour_booking.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class VNPayConfig {
    @Getter
    @Value("${spring.payment.vnPay.ipnUrl}")
    private String vnp_PayUrl;
    @Getter
    @Value("${spring.payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;
    @Value("${spring.payment.vnPay.tmnCode}")
    private String vnp_TmnCode ;
    @Getter
    @Value("${spring.payment.vnPay.hashSecret}")
    private String secretKey;


    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", "2.1.0");
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        return vnpParamsMap;
    }

}
