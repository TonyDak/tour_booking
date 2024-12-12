package com.tourbooking.tour_booking.dto.booking;

import com.tourbooking.tour_booking.dto.user.UserInfoRequest;
import com.tourbooking.tour_booking.entity.Bill;
import com.tourbooking.tour_booking.entity.Promotion;
import com.tourbooking.tour_booking.entity.Tour;
import com.tourbooking.tour_booking.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class BookingDetailRespone {
    String bill_id;
    String pin_code;
    String start_time;
    Integer total_days;
    Integer adult_quantity;
    Integer children_quantity;
    Integer total_quantity;
    String status;
    String avt;
    String tour_title;
    UserInfoRequest user;
    List<TravelerRespone> travelers;
    String special_requirement;
    Integer temp_price;
    Promotion promotion;
    Double tour_price;
    Integer total_price;

    public BookingDetailRespone(Bill bill) {
        this.bill_id = bill.getId();
        this.pin_code = bill.getPin_code();
        this.start_time = String.valueOf(bill.getStart_time());
        this.total_days = bill.getTour().getTotal_days();
        this.adult_quantity = bill.getTotal_adult();
        this.children_quantity = bill.getTotal_child();
        this.total_quantity = bill.getTotal_adult() + bill.getTotal_child();
        this.status = bill.getBill_status().name();
        this.avt = bill.getTour().getAvt();
        this.tour_title = bill.getTour().getTitle();
        this.user = new UserInfoRequest(bill.getUser());
        this.travelers = bill.getTravelers().stream().map(TravelerRespone::new).toList();
        this.special_requirement = bill.getSpecial_requirement();
        this.temp_price = (int) ((bill.getTour().getPrice() * bill.getTotal_adult()) + (bill.getTour().getPrice() * 0.6 * bill.getTotal_child()));
        this.promotion = bill.getPromotion();
        this.tour_price = bill.getTour().getPrice();
        this.total_price = bill.getTotal_price();
    }
}
