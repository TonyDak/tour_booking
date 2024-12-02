package com.tourbooking.tour_booking.dto.user;

import com.tourbooking.tour_booking.dto.booking.TravelerRespone;
import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Traveler;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TransactionDetailRespone {
    String bill_id;
    String bill_status;
    LocalDateTime booked_at;
    Integer total_days;
    String tour_title;
    String avt;
    LocalDate start_days;
    LocalDate end_days;
    Integer total_adult;
    Integer total_children;
    Integer total_price;
    List<TravelerRespone> travelers;

    public TransactionDetailRespone(Bill bill) {
        this.bill_id = bill.getId();
        this.bill_status = bill.getBill_status().getName();
        this.booked_at = bill.getBooked_at();
        this.total_days = bill.getTour().getTotal_days();
        this.tour_title = bill.getTour().getTitle();
        this.avt = bill.getTour().getAvt();
        this.start_days = bill.getTour().getStart_days();
        this.end_days = bill.getTour().getEnd_days();
        this.total_adult = (int) bill.getTravelers().stream().filter(traveler -> traveler.getType() == Traveler.TravelerType.ADULT).count();
        this.total_children = (int) bill.getTravelers().stream().filter(traveler -> traveler.getType() == Traveler.TravelerType.CHILD).count();
        this.total_price = bill.getTotal_price();
        this.travelers = bill.getTravelers().stream().map(TravelerRespone::new).collect(Collectors.toList());
    }
}
