package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.NotificationTraveller;
import com.tourbooking.tour_booking.exception.ResourceNotFoundException;
import com.tourbooking.tour_booking.repository.NotificationTravellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationTravellerService {

    @Autowired
    private NotificationTravellerRepository notificationTravellerRepository;

    /**
     * Tạo thông báo cho traveler từ một hóa đơn (Bill).
     *
     * @param message Nội dung thông báo.
     * @param bill    Hóa đơn liên quan đến thông báo.
     * @return Thông báo đã được lưu trong cơ sở dữ liệu.
     */
    @Transactional
    public NotificationTraveller createAndSendNotification(String message, Bill bill) {
        if (bill == null) {
            throw new IllegalArgumentException("Bill cannot be null");
        }


        NotificationTraveller notification = new NotificationTraveller();
        notification.setMessage(message);
        notification.setBill(bill);
        notification.setRead(false);
        NotificationTraveller savedNotification = notificationTravellerRepository.save(notification);


        String travelerId = bill.getTraveler().getId();
        sendNotificationToTraveler(travelerId, message);

        return savedNotification;
    }
    /**
     * Lấy danh sách thông báo chưa đọc của một traveler, sắp xếp theo thời gian tạo.
     *
     * @param travelerId ID của traveler.
     * @return Danh sách thông báo chưa đọc.
     */
    public List<NotificationTraveller> getUnreadNotificationsForTraveller(String travelerId) {
        if (travelerId == null || travelerId.isEmpty()) {
            throw new IllegalArgumentException("Traveler ID cannot be null or empty");
        }

        return notificationTravellerRepository.findByBillTravelerIdAndIsReadOrderByCreatedAtDesc(travelerId, false);
    }

    /**
     * Đánh dấu một thông báo là đã đọc.
     *
     * @param id ID của thông báo.
     * @return Thông báo sau khi đã cập nhật trạng thái.
     */
    @Transactional
    public NotificationTraveller markAsRead(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Notification ID cannot be null or empty");
        }

        NotificationTraveller notification = notificationTravellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        notification.setRead(true);
        return notificationTravellerRepository.save(notification);
    }

    public void sendNotificationToTraveler(String travelerId, String message) {
        System.out.println("Sending notification to traveler " + travelerId + ": " + message);
    }
}
