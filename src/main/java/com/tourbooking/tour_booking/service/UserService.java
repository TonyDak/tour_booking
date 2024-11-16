package com.tourbooking.tour_booking.service;

import java.util.UUID;

import com.tourbooking.tour_booking.dto.user.*;
import com.tourbooking.tour_booking.mapper.UserMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tourbooking.tour_booking.entity.User;
import com.tourbooking.tour_booking.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;


    public Page<AdminUserInfoRequest> getUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).map(userMapper::toAdminUserInfo);

    }
    public Page<AdminUserInfoRequest> getUsersByName(String userName, int page, int size) {
        return userRepository.findByNameContainingIgnoreCase(userName, PageRequest.of(page, size)).map(userMapper::toAdminUserInfo);
    }

    public Page<AdminUserInfoRequest> getUsersByEmail(String email, int page, int size) {
        return userRepository.findByEmailContainingIgnoreCase(email, PageRequest.of(page, size)).map(userMapper::toAdminUserInfo);
    }

    public Page<AdminUserInfoRequest> getUsersByPhone(String phone, int page, int size) {
        return userRepository.findByPhoneContainingIgnoreCase(phone, PageRequest.of(page, size)).map(userMapper::toAdminUserInfo);
    }


    public UserInfoRequest getUser(String id) {
        User user = userRepository.findById(id).orElseThrow();
        return userMapper.toUserInfo(user);
    }

    public UserInfoRequest getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        return userMapper.toUserInfo(user);
    }


    public void updateStatus(String id,UpdateStatusRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(request.getStatus());
        userRepository.save(user);
    }

    public UserUpdateRequest updateUser(UserUpdateRequest userUpdateRequest) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        userMapper.updateUserFromDto(userUpdateRequest, user);
        userRepository.save(user);
        return userUpdateRequest;
    }

    public AdminUserUpdateRequest AdminUpdateUser(AdminUserUpdateRequest adminUserUpdateRequest) {
        User user = userRepository.findByEmail(adminUserUpdateRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.AdminUpdateUserFromDto(adminUserUpdateRequest, user);
        userRepository.save(user);
        return adminUserUpdateRequest;
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        //check if new password is the same as the old password
        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password is the same as the old password");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        var user = userRepository.findByEmail(forgotPasswordRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        // Generate a reset token
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userRepository.save(user);
        // Send email with reset token
        sendResetEmail(forgotPasswordRequest.getEmail(), token);
    }

    private void sendResetEmail(String email, String token) throws MessagingException {
        String resetUrl = "http://localhost:8080/api/v1/users/reset-password/" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Password Reset Request");
        helper.setText("<p>To reset your password, click the link below:</p>"
                + "<a href=\"" + resetUrl + "\">Reset Password</a>", true);

        mailSender.send(message);
    }

    public void resetPassword(String token, ResetPasswordRequest resetPasswordRequest) {
        // Check if token is valid
        var user = userRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid token"));
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            throw new IllegalStateException("Passwords are not the same");
        }
        // Update user password
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setToken(null);
        userRepository.save(user);
    }
}
