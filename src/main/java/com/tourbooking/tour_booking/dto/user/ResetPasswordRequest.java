package com.tourbooking.tour_booking.dto.user;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    String newPassword;
    String confirmPassword;
}
