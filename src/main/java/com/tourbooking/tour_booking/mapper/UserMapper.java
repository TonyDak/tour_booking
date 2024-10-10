package com.tourbooking.tour_booking.mapper;

import com.tourbooking.tour_booking.dto.user.UserCreate;
import com.tourbooking.tour_booking.dto.user.UserInfoUpdate;
import com.tourbooking.tour_booking.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoUpdate toUserInfoUpdate(User user);

    User toUser(UserCreate userCreate);

    void updateUserFromDto(UserInfoUpdate dto, @MappingTarget User entity);
}
