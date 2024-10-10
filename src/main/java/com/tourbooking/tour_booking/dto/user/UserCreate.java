package com.tourbooking.tour_booking.dto.user;

import com.tourbooking.tour_booking.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value
@Valid
public class UserCreate implements Serializable {
    @Email(message = "Email should be valid")
    String email;
    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone number should start with 0 and be 10 digits long")
    String phone_number;
    String user_name;
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    String password;
    String dob;
}