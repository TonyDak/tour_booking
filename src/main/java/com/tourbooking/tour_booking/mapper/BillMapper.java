package com.tourbooking.tour_booking.mapper;

import com.tourbooking.tour_booking.dto.booking.BookingRequest;
import com.tourbooking.tour_booking.entity.Bill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillMapper {
    Bill toBill(BookingRequest BookingRequest);
}
