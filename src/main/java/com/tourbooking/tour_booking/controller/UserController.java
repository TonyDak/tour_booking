package com.tourbooking.tour_booking.controller;

import java.util.List;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.user.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tourbooking.tour_booking.service.UserService;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //hiển thị đầy đủ thông tin user trừ password cho admin
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<AdminUserInfoRequest>> getUsers(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size){
        ApiResponse<Page<AdminUserInfoRequest>> response = new ApiResponse<>();
        response.setMessage("Users");
        response.setResult(userService.getUsers(page, size));
        return response;
    }

    @GetMapping("/admin/search-name")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<AdminUserInfoRequest>> getUsersByName(@RequestParam String name,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
        ApiResponse<Page<AdminUserInfoRequest>> response = new ApiResponse<>();
        response.setMessage("Users");
        response.setResult(userService.getUsersByName(name, page, size));
        return response;
    }

    @GetMapping("/admin/search-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<AdminUserInfoRequest>> getUsersByEmail(@RequestParam String email,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
        ApiResponse<Page<AdminUserInfoRequest>> response = new ApiResponse<>();
        response.setMessage("Users");
        response.setResult(userService.getUsersByEmail(email, page, size));
        return response;
    }

    @GetMapping("/admin/search-phone")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<AdminUserInfoRequest>> getUsersByPhone(@RequestParam String phone,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
        ApiResponse<Page<AdminUserInfoRequest>> response = new ApiResponse<>();
        response.setMessage("Users");
        response.setResult(userService.getUsersByPhone(phone, page, size));
        return response;
    }

    //người dùng hiển thị thông tin cá nhân của mình
    @GetMapping("/my-info")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<UserInfoRequest> getMyInfo(){
        ApiResponse<UserInfoRequest> response = new ApiResponse<>();
        response.setMessage("User info");
        response.setResult(userService.getMyInfo());
        return response;
    }

    //trong màn hình danh sách user của admin, thì thay đổi trạng thái ngay dòng user đó
    @PutMapping("/admin/update-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> updateStatus(@PathVariable String id,@RequestBody UpdateStatusRequest request) {
        userService.updateStatus(id, request);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Status updated successfully");
        return response;
    }

    //người dùng tự update user của mình dựa vào token đã đăng nhập
    @PutMapping("/update-user")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<UserUpdateRequest> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        ApiResponse<UserUpdateRequest> response = new ApiResponse<>();
        response.setMessage("User updated successfully");
        response.setResult(userService.updateUser(userUpdateRequest));
        return response;
    }

    //admin update user của người dùng
    @PutMapping("/admin/update-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AdminUserUpdateRequest> updateUser(@RequestBody AdminUserUpdateRequest adminUserUpdateRequest){
        ApiResponse<AdminUserUpdateRequest> response = new ApiResponse<>();
        response.setMessage("User updated successfully");
        response.setResult(userService.AdminUpdateUser(adminUserUpdateRequest));
        return response;
    }

    //đổi mật khẩu
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        userService.changePassword(changePasswordRequest);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Password changed successfully");
        return response;
    }

    //quên mật khẩu
    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        userService.forgotPassword(forgotPasswordRequest);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Email sent successfully");
        return response;
    }

    //reset mật khẩu
    @PostMapping("/reset-password/{token}")
    public ApiResponse<Void> resetPassword(@PathVariable String token, @RequestBody ResetPasswordRequest resetPasswordRequest){
        userService.resetPassword(token, resetPasswordRequest);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Password reset successfully");
        return response;
    }

    //transaction history
}
