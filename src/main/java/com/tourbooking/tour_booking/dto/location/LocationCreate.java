package com.tourbooking.tour_booking.dto.location;

import jakarta.validation.Valid;
import lombok.Value;

/**
 * DTO for {@link com.tourbooking.tour_booking.entity.Location}
 */

@Valid
@Value
public class LocationCreate {
    String name;


}
