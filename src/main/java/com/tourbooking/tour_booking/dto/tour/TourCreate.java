package com.tourbooking.tour_booking.dto.tour;

import com.tourbooking.tour_booking.dto.gallery.GalleryCreate;
import com.tourbooking.tour_booking.dto.itinerary.ItineraryCreate;
import com.tourbooking.tour_booking.dto.schedule.ScheduleCreate;
import com.tourbooking.tour_booking.entity.Tour;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link Tour}
 */

@Value
@Valid
public class TourCreate {
    @NotNull
    String title;

    String slug;
    @NotNull
    Double price;

    String avt;

    String total_days;
    String start_days;
    String end_days;
    String bookable_start_date;
    String bookable_end_date;

    String policy;

    String min_booking_traveller;
    String locationId;
    List<GalleryCreate> galleries;
    List<ScheduleCreate> schedules;
    List<ItineraryCreate> itineraries;




}
