package com.tourbooking.tour_booking.controller;

import java.util.List;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.user.UserCreate;
import com.tourbooking.tour_booking.dto.user.UserInfoUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tourbooking.tour_booking.service.UserService;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserInfoUpdate>> getUsers(){
        ApiResponse<List<UserInfoUpdate>> response = new ApiResponse<>();
        response.setMessage("Users");
        response.setResult(userService.getUsers());
        return response;
    }

    @PostMapping
    public ApiResponse<UserCreate> createUser(@RequestBody @Valid UserCreate userCreate){
        ApiResponse<UserCreate> response = new ApiResponse<>();
        response.setMessage("User created");
        response.setResult(userService.createUser(userCreate));
        return response;
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserInfoUpdate> getMyInfo(){
        ApiResponse<UserInfoUpdate> response = new ApiResponse<>();
        response.setMessage("User info");
        response.setResult(userService.getMyInfo());
        return response;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public UserInfoUpdate updateUser(@PathVariable String id, @RequestBody UserInfoUpdate userInfo){
        return userService.updateUser(id, userInfo);
    }

}
