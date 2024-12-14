package com.tourbooking.tour_booking.dto.user;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.tourbooking.tour_booking.entity.Role}
 */
@Value
public class RoleRequest implements Serializable {
    String name;
    String description;
    Set<String> permissions;
}