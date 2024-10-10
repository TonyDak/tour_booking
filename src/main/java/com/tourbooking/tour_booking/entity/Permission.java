package com.tourbooking.tour_booking.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    CREATE,
    READ,
    UPDATE,
    DELETE;

    public String getName() {
        return this.name();
    }
}
