package com.tourbooking.tour_booking.controller;

import java.util.List;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.user.*;
import jakarta.mail.MessagingException;
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
    public ApiResponse<List<UserInfoRequest>> getUsers(){
        ApiResponse<List<UserInfoRequest>> response = new ApiResponse<>();
        response.setMessage("Users");
        response.setResult(userService.getUsers());
        return response;
    }


    @GetMapping("/my-info")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<UserInfoRequest> getMyInfo(){
        ApiResponse<UserInfoRequest> response = new ApiResponse<>();
        response.setMessage("User info");
        response.setResult(userService.getMyInfo());
        return response;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> updateStatus(@RequestBody UpdateStatusRequest request) {
        userService.updateStatus(request);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Status updated successfully");
        return response;
    }

    @PutMapping("/update-user")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<UserUpdateRequest> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        ApiResponse<UserUpdateRequest> response = new ApiResponse<>();
        response.setMessage("User updated successfully");
        response.setResult(userService.updateUser(userUpdateRequest));
        return response;
    }
    @PutMapping("/admin/update-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AdminUserUpdateRequest> updateUser(@RequestBody AdminUserUpdateRequest adminUserUpdateRequest){
        ApiResponse<AdminUserUpdateRequest> response = new ApiResponse<>();
        response.setMessage("User updated successfully");
        response.setResult(userService.AdminUpdateUser(adminUserUpdateRequest));
        return response;
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        userService.changePassword(changePasswordRequest);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Password changed successfully");
        return response;
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        userService.forgotPassword(forgotPasswordRequest);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Email sent successfully");
        return response;
    }

    @PostMapping("/reset-password/{token}")
    public ApiResponse<Void> resetPassword(@PathVariable String token, @RequestBody ResetPasswordRequest resetPasswordRequest){
        userService.resetPassword(token, resetPasswordRequest);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Password reset successfully");
        return response;
    }
}
