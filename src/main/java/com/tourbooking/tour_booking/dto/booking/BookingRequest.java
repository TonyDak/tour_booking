package com.tourbooking.tour_booking.dto.booking;

import com.tourbooking.tour_booking.entity.Traveler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Value
@Valid
public class BookingRequest {
    String bill_id;
    String tour_id;
    String user_name;
    @Email(message = "Email should be valid")
    String email;
    @Pattern(regexp = "^0[0-9]{9}$", message = "Phone number should start with 0 and be 10 digits long")
    String phone_number;
    List<TravelerRequest> travelers;
    String special_requirement;
    String other_requirement;
    String promotion_code;

}
