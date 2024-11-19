package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.entity.NotificationTraveller;
import com.tourbooking.tour_booking.service.NotificationTravellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traveller/notifications")
public class NotificationTravellerController {

    @Autowired
    private NotificationTravellerService notificationTravellerService;

    @GetMapping("/unread/{travelerId}")
    public ResponseEntity<List<NotificationTraveller>> getUnreadNotificationsForTraveller(@PathVariable String travelerId) {
        return  ResponseEntity.ok(notificationTravellerService.getUnreadNotificationsForTraveller(travelerId));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationTraveller> markAsRead(@PathVariable String id) {
        return ResponseEntity.ok(notificationTravellerService.markAsRead(id));
    }
}