package com.tourbooking.tour_booking.mapper;

import com.tourbooking.tour_booking.dto.booking.BookingDraftRequest;
import com.tourbooking.tour_booking.dto.booking.BookingDraftRespone;
import com.tourbooking.tour_booking.dto.booking.BookingRequest;
import com.tourbooking.tour_booking.dto.booking.TravelerRequest;
import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Traveler;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillMapper {
    Bill toBill(BookingDraftRequest bookingDraftRequest);
    BookingDraftRespone toBookingDraftRespone(Bill bill);
    Traveler toTraveler(TravelerRequest travelerRequest);


}
