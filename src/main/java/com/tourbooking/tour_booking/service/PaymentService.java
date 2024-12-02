package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.config.VNPayConfig;
import com.tourbooking.tour_booking.dto.payment.VNPayRequest;
import com.tourbooking.tour_booking.dto.payment.VNPayResponse;
import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Tour;
import com.tourbooking.tour_booking.repository.BillRepository;
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPayConfig vnPayConfig;
    private final JavaMailSender mailSender;
    private final BillRepository billRepository;


    //create vnPay payment
    public VNPayResponse createVnPayPayment(HttpServletRequest request, VNPayRequest vnPayRequest) throws ParseException {
        Bill bill = billRepository.findById(vnPayRequest.getBill_id()).orElseThrow(() -> new RuntimeException("Bill not found"));
        long amount = bill.getTotal_price() * 100L;
        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Command", "pay");
        vnpParamsMap.put("vnp_TxnRef", bill.getId());
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang dat tour:" +  bill.getId());
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
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = inputFormat.parse(bill.getBooked_at().toString());
        String booked_at = formatter.format(date);
        //lay create cua bill
        vnpParamsMap.put("vnp_CreateDate", booked_at);
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


    public VNPayResponse verifyVNPayTransaction(HttpServletRequest request, String billId) throws UnsupportedEncodingException, MessagingException {
        // So sánh mã hash vừa tạo với mã từ VNPay
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new RuntimeException("Bill not found"));
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
                            bill.setBill_status(Bill.BillStatus.PAID);
                            billRepository.save(bill);
                            sendEmailPaymentSucces(bill.getUser().getEmail(), bill.getTour().getTitle(), bill.getId(), bill.getBooked_at(), bill.getTotal_price());
                            return VNPayResponse.builder()
                                    .code("00")
                                    .message("VNPay - Thanh toán thành công")
                                    .paymentUrl("").build();
                        }
                        else
                        {
                            // Here Code update PaymnentStatus = 2 into your Database bill
                            bill.setBill_status(Bill.BillStatus.FAILED);
                            billRepository.save(bill);
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

    public void sendEmailPaymentSucces(String email, String tourName, String billId, LocalDateTime booked_at, Integer price) throws MessagingException {
        //send email payment success
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        // Inside your method
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormatter.format(price);

        helper.setTo(email);
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Xác nhận thanh toán thành công đặt tour " + tourName + " tại Tour Booking");

        String content = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px; margin: 20px auto; background-color: #ffffff;\">\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 40px 30px; background-color: #4CAF50; text-align: center;\">\n" +
                "                <h1 style=\"color: #ffffff; margin: 0;\">Payment Successful</h1>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 40px 30px;\">\n" +
                "                <p style=\"font-size: 16px; line-height: 24px; margin: 0 0 20px;\">Dear Customer,</p>\n" +
                "                <p style=\"font-size: 16px; line-height: 24px; margin: 0 0 20px;\">Thank you for your payment. Your transaction has been successfully processed.</p>\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"margin-bottom: 20px;\">\n" +
                "                    <tr>\n" +
                "                        <td style=\"padding: 10px; border-bottom: 1px solid #eeeeee; font-weight: bold;\">Payment Details:</td>\n" +
                "                        <td style=\"padding: 10px; border-bottom: 1px solid #eeeeee;\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style=\"padding: 10px; border-bottom: 1px solid #eeeeee;\">Amount Paid:</td>\n" +
                "                        <td style=\"padding: 10px; border-bottom: 1px solid #eeeeee;\">"+formattedPrice+"</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style=\"padding: 10px; border-bottom: 1px solid #eeeeee;\">Bill ID:</td>\n" +
                "                        <td style=\"padding: 10px; border-bottom: 1px solid #eeeeee;\">"+billId+"</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style=\"padding: 10px; border-bottom: 1px solid #eeeeee;\">Date:</td>\n" +
                "                        <td style=\"padding: 10px; border-bottom: 1px solid #eeeeee;\">"+booked_at+"</td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "                <p style=\"font-size: 16px; line-height: 24px; margin: 0 0 20px;\">If you have any questions or concerns regarding this transaction, please don't hesitate to contact our customer support team.</p>\n" +
                "                <p style=\"font-size: 16px; line-height: 24px; margin: 0;\">Thank you for your business!</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px 30px; background-color: #f8f8f8; text-align: center; font-size: 14px; color: #888888;\">\n" +
                "                <p style=\"margin: 0;\">This is an automated email. Please do not reply.</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>";
        helper.setText(content, true);
        // Gửi email
        mailSender.send(message);
    }
}
