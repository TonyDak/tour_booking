package com.tourbooking.tour_booking.dto.itinerary;

import com.tourbooking.tour_booking.dto.placevisit.PlaceVisitCreate;
import jakarta.validation.Valid;
import lombok.Value;

import java.util.List;

@Value
@Valid
public class ItineraryCreate {
    String title;
    Integer day_no;
    List<PlaceVisitCreate> placeVisits;

}
