package com.tourbooking.tour_booking.dto.booking;

import com.tourbooking.tour_booking.entity.Traveler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Valid
public class TravelerRespone {
    String name;
    String email;
    String phone_number;
    Traveler.TravelerType type;

    public TravelerRespone(Traveler traveler) {
        this.name = traveler.getName();
        this.email = traveler.getEmail();
        this.phone_number = traveler.getPhone_number();
        this.type = traveler.getType();
    }
}
