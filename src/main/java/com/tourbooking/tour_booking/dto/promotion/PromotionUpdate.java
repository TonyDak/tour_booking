package com.tourbooking.tour_booking.dto.promotion;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

@Value
@Getter
@Setter
public class PromotionUpdate implements Serializable {
    @NotBlank(message = "Code is mandatory")
    String code;
    String title;
    String content;
    String thumbnail;
    String expireddate;
}
