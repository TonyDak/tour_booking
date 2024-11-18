package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.config.VNPayConfig;
import com.tourbooking.tour_booking.dto.payment.VNPayResponse;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPayConfig vnPayConfig;
    private final JavaMailSender mailSender;


    //create vnPay payment
    public VNPayResponse createVnPayPayment(HttpServletRequest request, String billId, String price) {
        long amount = Integer.parseInt(price) * 100L;
        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Command", "pay");
        vnpParamsMap.put("vnp_TxnRef", billId);
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang dat tour:" +  billId);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", getIpAddress(request));
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_OrderType", "other");
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", vnPayConfig.getVnp_ReturnUrl());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        //lay create cua bill
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
        //build query url
        String queryUrl = getPaymentURL(vnpParamsMap, true);
        String hashData = getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }


    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }


    public VNPayResponse verifyVNPayTransaction(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        // So sánh mã hash vừa tạo với mã từ VNPay
        if (checkSum(request)) {
            boolean checkOrderId = true; // vnp_TxnRef exists in your database
            boolean checkAmount = true; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the
            boolean checkOrderStatus = true; // PaymnentStatus = 0 (pending)
            if(checkOrderId)
            {
                if(checkAmount)
                {
                    if (checkOrderStatus)
                    {
                        if ("00".equals(request.getParameter("vnp_ResponseCode")))
                        {
                            // Here Code update PaymnentStatus = 1 into your Database bill

                            sendEmailPaymentSucces("duckg2083999@gmail.com", "tourName", "billId", LocalDateTime.now(), "userName", "phone", "address", 1000000);
                            return VNPayResponse.builder()
                                    .code("00")
                                    .message("VNPay - Thanh toán thành công")
                                    .paymentUrl("").build();
                        }
                        else
                        {
                            // Here Code update PaymnentStatus = 2 into your Database bill

                            return VNPayResponse.builder()
                                    .code("01")
                                    .message("VNPay - Thanh toán thất bại")
                                    .paymentUrl("").build();
                        }
                    }
                    else
                    {
                        return VNPayResponse.builder()
                                .code("02")
                                .message("VNPay - Đơn hàng đã được xử lý")
                                .paymentUrl("").build();
                    }
                }
                else
                {
                    return VNPayResponse.builder()
                            .code("04")
                            .message("VNPay - Số tiền không hợp lệ")
                            .paymentUrl("").build();
                }
            }
            else
            {
                return VNPayResponse.builder()
                        .code("05")
                        .message("VNPay - Mã đơn hàng không hợp lệ")
                        .paymentUrl("").build();
            }
        } else {
            return VNPayResponse.builder()
                    .code("99")
                    .message("VNPay - Mã xác thực không hợp lệ")
                    .paymentUrl("").build();
        }
    }

    // Hàm xác thực `vnp_SecureHash`
    public boolean checkSum(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()),
                        URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
            }
        }
        // Lấy và xóa vnp_SecureHash từ fields
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        // Sắp xếp các trường theo thứ tự bảng chữ cái và tạo chuỗi dữ liệu
        StringBuilder data = new StringBuilder();
        fields.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> data.append(entry.getKey()).append("=").append(entry.getValue()).append("&"));

        if (data.length() > 0) data.setLength(data.length() - 1); // Xóa ký tự `&` cuối cùng

        // Tính toán mã băm với secretKey
        String calculatedHash = hmacSHA512(vnPayConfig.getSecretKey(), data.toString());
        return calculatedHash.equals(vnp_SecureHash);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
    public static String getPaymentURL(Map<String, String> paramsMap, boolean encodeKey) {
        return paramsMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(entry ->
                        (encodeKey ? URLEncoder.encode(entry.getKey(),
                                StandardCharsets.US_ASCII)
                                : entry.getKey()) + "=" +
                                URLEncoder.encode(entry.getValue()
                                        , StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));
    }

    public void sendEmailPaymentSucces(String email, String tourName, String billId, LocalDateTime booked_at, String userName,String phone, String address, Integer price) throws MessagingException {
        //send email payment success
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Xác nhận thanh toán thành công đặt tour #" + billId);

        String content = "<p>Kính gửi Quý khách hàng,</p>"
                + "<p>Chúng tôi xin thông báo rằng khoản thanh toán của bạn cho đơn hàng đã được xử lý thành công. Thông tin chi tiết như sau:</p>"
                + "<ul>"
                    +"<ul><b>Thông tin khách hàng đặt tour</b>"
                        + "<li><b>Tên khách hàng:</b> " + userName + "</li>"
                        + "<li><b>Email:</b> " + email + "</li>"
                        + "<li><b>Số điện thoại:</b> " + phone + "</li>"
                        + "<li><b>Địa chỉ:</b> " + address + "</li>"
                    +"</ul>"
                + "<li><b>Tên tour thanh toán:</b> " + tourName + "</li>"
                + "<li><b>Mã đơn hàng:</b> " + billId + "</li>"
                + "<li><b>Ngày thanh toán:</b> " + booked_at + "</li>"
                + "<li><b>Số tiền thanh toán:</b> " + String.format("%,d VND", price) + "</li>"
                + "</ul>"
                + "<p>Xin cảm ơn quý khách đã tin tưởng và sử dụng dịch vụ của chúng tôi.</p>"
                + "<p>Trân trọng,</p>"
                + "<p>Đội ngũ hỗ trợ</p>";
        helper.setText(content, true);
        // Gửi email
        mailSender.send(message);
    }
}
