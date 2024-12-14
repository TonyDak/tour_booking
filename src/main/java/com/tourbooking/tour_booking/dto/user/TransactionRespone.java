package com.tourbooking.tour_booking.dto.user;

import com.tourbooking.tour_booking.entity.Bill;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionRespone {
    String bill_id;
    String bill_status;
    String tour_title;
    String avt;
    LocalDate start_days;
    LocalDate end_days;
    Integer total_adult;
    Integer total_children;
    Integer total_price;

    public TransactionRespone(Bill bill) {
        this.bill_id = bill.getId();
        this.bill_status = bill.getBill_status().name();
        this.tour_title = bill.getTour().getTitle();
        this.avt = bill.getTour().getAvt();
        this.start_days = bill.getTour().getStart_days();
        this.end_days = bill.getTour().getEnd_days();
        //check type of traveler
        this.total_adult = (int) bill.getTravelers().stream().filter(traveler -> traveler.getType().name().equals("ADULT")).count();
        this.total_children = (int) bill.getTravelers().stream().filter(traveler -> traveler.getType().name().equals("CHILD")).count();
        this.total_price = bill.getTotal_price();
    }
}
