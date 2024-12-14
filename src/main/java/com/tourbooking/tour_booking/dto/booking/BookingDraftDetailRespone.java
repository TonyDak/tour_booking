package com.tourbooking.tour_booking.dto.booking;

import com.tourbooking.tour_booking.entity.Bill;
import lombok.Data;

@Data
public class BookingDraftDetailRespone {
    String tour_id;
    String tour_title;
    String avt;
    String location;
    Integer adult_quantity;
    Integer children_quantity;
    Integer total_quantity;
    Double price;
    Integer total_days;
    String start_time;

    public BookingDraftDetailRespone(Bill bill) {
        this.tour_id = bill.getTour().getId();
        this.tour_title = bill.getTour().getTitle();
        this.avt = bill.getTour().getAvt();
        this.location = bill.getTour().getLocation().getName();
        this.adult_quantity = bill.getTotal_adult();
        this.children_quantity = bill.getTotal_child();
        this.total_quantity = adult_quantity + children_quantity;
        this.price = bill.getTour().getPrice();
        this.total_days = bill.getTour().getTotal_days();
        this.start_time = String.valueOf(bill.getStart_time());
    }
}
