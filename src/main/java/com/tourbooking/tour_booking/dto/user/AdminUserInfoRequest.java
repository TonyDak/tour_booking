package com.tourbooking.tour_booking.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDate;

@Value
@Getter
@Setter
public class AdminUserInfoRequest {
    private String email;
    private String phone_number;
    private String user_name;
    private LocalDate dob;
    private Integer status;
}
