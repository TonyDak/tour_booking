package com.tourbooking.tour_booking.mapper;

import com.tourbooking.tour_booking.dto.auth.RegisterRequest;
import com.tourbooking.tour_booking.dto.user.AdminUserInfoRequest;
import com.tourbooking.tour_booking.dto.user.AdminUserUpdateRequest;
import com.tourbooking.tour_booking.dto.user.UserInfoRequest;
import com.tourbooking.tour_booking.dto.user.UserUpdateRequest;
import com.tourbooking.tour_booking.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoRequest toUserInfo(User user);
    AdminUserInfoRequest toAdminUserInfo(User user);

    User toUser(RegisterRequest registerRequest);

    void updateUserFromDto(UserUpdateRequest dto, @MappingTarget User entity);
    void AdminUpdateUserFromDto(AdminUserUpdateRequest dto, @MappingTarget User entity);
}
