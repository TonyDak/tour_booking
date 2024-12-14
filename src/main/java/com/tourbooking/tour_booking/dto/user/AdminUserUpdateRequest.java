package com.tourbooking.tour_booking.dto.user;


import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.tourbooking.tour_booking.entity.User}
 */
@Value
@Getter
@Setter
public class AdminUserUpdateRequest implements Serializable {
    String email;
    String phone_number;
    String user_name;
    String dob;
    String role;
}