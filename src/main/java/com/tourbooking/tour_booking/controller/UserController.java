package com.tourbooking.tour_booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tourbooking.tour_booking.entity.User;
import com.tourbooking.tour_booking.service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public List<User> getUsers(){
        return userService.getUsers();
    }
}
