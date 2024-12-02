package com.tourbooking.tour_booking.dto.user;


import com.tourbooking.tour_booking.entity.User;
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
public class UserInfoRequest implements Serializable {
    String email;
    String phone_number;
    String user_name;
    String dob;

    public UserInfoRequest(User user) {
        this.email = user.getEmail();
        this.phone_number = user.getPhone_number();
        this.user_name = user.getUser_name();
        this.dob = String.valueOf(user.getDob());
    }
}