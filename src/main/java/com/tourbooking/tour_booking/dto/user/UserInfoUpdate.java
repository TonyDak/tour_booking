package com.tourbooking.tour_booking.dto.user;


import com.tourbooking.tour_booking.entity.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link com.tourbooking.tour_booking.entity.User}
 */
@Value
@Getter
@Setter
public class UserInfoUpdate implements Serializable {
    String email;
    String phone_number;
    String user_name;
    String dob;
    List<String> roles;
}