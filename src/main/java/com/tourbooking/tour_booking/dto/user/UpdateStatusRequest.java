package com.tourbooking.tour_booking.dto.user;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.tourbooking.tour_booking.entity.User}
 */
@Value
@Data
public class UpdateStatusRequest implements Serializable {

    int status;
}