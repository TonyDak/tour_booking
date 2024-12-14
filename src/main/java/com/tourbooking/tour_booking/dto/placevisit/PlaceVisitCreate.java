package com.tourbooking.tour_booking.dto.placevisit;

import com.tourbooking.tour_booking.entity.Place;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    Integer order_number;
    Place place;
}
