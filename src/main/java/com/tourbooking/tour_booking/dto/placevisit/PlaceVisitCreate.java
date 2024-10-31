package com.tourbooking.tour_booking.dto.placevisit;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import com.tourbooking.tour_booking.entity.PlaceVisits;

/**
 * DTO for {@link PlaceVisits}
 */
@Value
@Valid
public class PlaceVisitCreate {
    String start_time;
    String end_time;
    String description;
    String place_id;
}
