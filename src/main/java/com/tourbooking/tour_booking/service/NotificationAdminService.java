    package com.tourbooking.tour_booking.service;

    import com.tourbooking.tour_booking.entity.Payment;
    import com.tourbooking.tour_booking.entity.NotificationAdminEntity;
    import com.tourbooking.tour_booking.entity.Tour;
    import com.tourbooking.tour_booking.exception.ResourceNotFoundException;
    import com.tourbooking.tour_booking.repository.NotificationAdminRepository;
    import com.tourbooking.tour_booking.repository.TourRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.mail.javamail.JavaMailSender;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import jakarta.mail.MessagingException;
    import jakarta.mail.internet.MimeMessage;
    import org.springframework.mail.javamail.MimeMessageHelper;

    import java.util.List;

    @Service
    public class NotificationAdminService {

        private final JavaMailSender mailSender;

        @Autowired
        private TourRepository tourRepository;

        @Autowired
        private NotificationAdminRepository notificationAdminRepository;

        @Autowired
        public NotificationAdminService(JavaMailSender mailSender) {
            this.mailSender = mailSender;
        }


        @Transactional
        public NotificationAdminEntity createNotificationToAdmin(String message, Payment payment,
                                                                        String adminEmail, String userName,
                                                                        String billId) throws MessagingException {

            if (payment == null) {
                throw new IllegalArgumentException("Payment cannot be null");
            }

            // Step 1: Create the notification and save to database
            NotificationAdminEntity notification = new NotificationAdminEntity();
            notification.setMessage(message);

            Tour tour = tourRepository.findById(payment.getTour().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + payment.getTour().getId()));

            notification.setTour(tour);
            notification.setRead(false);
            NotificationAdminEntity savedNotification = notificationAdminRepository.save(notification);

            // Step 2: Send the email notification to admin
            sendNotificationToAdmin(adminEmail, userName, billId);

            return savedNotification;
        }


        public void sendNotificationToAdmin(String adminEmail, String userName, String billId) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(adminEmail);
                message.setSubject("Thông báo: Đơn hàng thanh toán thành công #" + billId);
                String content = "<p>Khách hàng " + userName + " đã thanh toán thành công đơn hàng #" + billId + ".</p>"
                        + "<p>Vui lòng kiểm tra và xử lý yêu cầu.</p>";

                helper.setText(content, true);
                mailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
                throw new RuntimeException("Lỗi khi gửi email: " + e.getMessage(), e);
            }
        }


        public List<NotificationAdminEntity> getUnreadNotifications() {
            return notificationAdminRepository.findByIsReadOrderByCreatedAtDesc(false);
        }

        @Transactional
        public NotificationAdminEntity markAsRead(String id) {
            NotificationAdminEntity notification = notificationAdminRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

            notification.setRead(true);
            return notificationAdminRepository.save(notification);
        }
    }
