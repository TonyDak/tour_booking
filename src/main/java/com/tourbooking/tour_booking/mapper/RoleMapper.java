package com.tourbooking.tour_booking.mapper;

import com.tourbooking.tour_booking.dto.user.RoleRequest;
import com.tourbooking.tour_booking.dto.user.RoleResponse;
import com.tourbooking.tour_booking.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);

    Role toRole(RoleRequest roleRequest);

    void updateRoleFromDto(RoleRequest dto,@MappingTarget Role entity);
}
