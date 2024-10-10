package com.tourbooking.tour_booking.service;

import com.tourbooking.tour_booking.dto.user.RoleRequest;
import com.tourbooking.tour_booking.dto.user.RoleResponse;
import com.tourbooking.tour_booking.entity.Permission;
import com.tourbooking.tour_booking.mapper.RoleMapper;
import com.tourbooking.tour_booking.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest roleRequest) {
        var role = roleMapper.toRole(roleRequest);
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public RoleResponse getRole(String id) {
        var role = roleRepository.findById(id).orElseThrow(()-> new RuntimeException("Role not found"));
        return roleMapper.toRoleResponse(role);
    }

    public RoleResponse updateRole(String id, RoleRequest roleRequest) {
        var role = roleRepository.findById(id).orElseThrow();
        roleMapper.updateRoleFromDto(roleRequest, role);
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);

    }

    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }

    public Set<RoleResponse> getRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleResponse)
                .collect(Collectors.toSet());
    }

}
