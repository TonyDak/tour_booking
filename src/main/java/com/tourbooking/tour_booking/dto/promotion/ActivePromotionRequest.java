package com.tourbooking.tour_booking.dto.promotion;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.tourbooking.tour_booking.entity.Promotion}
 */
@Value
public class ActivePromotionRequest implements Serializable {
    String promotionId;
    Integer active;
}