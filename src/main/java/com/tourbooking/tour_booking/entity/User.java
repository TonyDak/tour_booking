package com.tourbooking.tour_booking.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    private String Id;
    private String email;
    private String phone_number;
    private String user_name;
    private String password;
    private Date dob;

}