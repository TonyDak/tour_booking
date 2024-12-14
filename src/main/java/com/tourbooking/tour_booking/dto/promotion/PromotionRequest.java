package com.tourbooking.tour_booking.dto.promotion;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.tourbooking.tour_booking.entity.Promotion}
 */
@Value
public class PromotionRequest implements Serializable {
    @NotNull(message = "active is required")
    Integer active;
    @NotNull(message = "code is required")
    String code;
    String description;
    @NotNull(message = "discount is required")
    Integer discount;
    @NotNull(message = "max_discount is required")
    Integer max_discount;
    Integer min_order;
    @NotNull(message = "name is required")
    String name;
    @NotNull(message = "type is required")
    Integer type;
    @NotNull(message = "stock is required")
    Integer stock;
    @NotNull(message = "start_time is required")
    String start_time;
    @NotNull(message = "end_time is required")
    String end_time;
}