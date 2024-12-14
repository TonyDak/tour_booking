package com.tourbooking.tour_booking.dto.promotion;

import jakarta.validation.Valid;
import com.tourbooking.tour_booking.entity.Promotion;
import jakarta.validation.constraints.NotEmpty;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Promotion}
 */


@Value
@Valid
public class PromotionCreate implements Serializable {

    @NotEmpty(message = "Code cannot be empty")
    String code;

    @NotEmpty(message = "Title cannot be empty")
    String title;

    @NotEmpty(message = "Content cannot be empty")
    String content;

    String thumbnail;

    @NotEmpty(message = "Expired date cannot be empty")
    String expireddate;
}
