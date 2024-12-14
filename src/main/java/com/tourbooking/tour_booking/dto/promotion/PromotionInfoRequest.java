package com.tourbooking.tour_booking.dto.promotion;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.tourbooking.tour_booking.entity.Promotion}
 */
@Value
public class PromotionInfoRequest implements Serializable {
    Integer active;
    String code;
    String description;
    Integer discount;
    Integer max_discount;
    Integer min_order;
    String name;
    Integer type;
    Integer stock;
    String start_time;
    String end_time;
}