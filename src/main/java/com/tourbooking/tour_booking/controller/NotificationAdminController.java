package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.entity.NotificationAdminEntity;
import com.tourbooking.tour_booking.service.NotificationAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notifications")
public class NotificationAdminController {

    @Autowired
    private NotificationAdminService notificationAdminService;

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationAdminEntity>> getUnreadNotifications() {
        return ResponseEntity.ok(notificationAdminService.getUnreadNotifications());
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationAdminEntity> markAsRead(@PathVariable String id) {
        return ResponseEntity.ok(notificationAdminService.markAsRead(id));
    }
}