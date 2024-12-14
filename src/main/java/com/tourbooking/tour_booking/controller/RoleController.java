package com.tourbooking.tour_booking.controller;

import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.user.RoleRequest;
import com.tourbooking.tour_booking.dto.user.RoleResponse;
import com.tourbooking.tour_booking.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest){
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setMessage("Role created");
        response.setResult(roleService.createRole(roleRequest));
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRole(@PathVariable String id){
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setMessage("Role");
        response.setResult(roleService.getRole(id));
        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> updateRole(@PathVariable String id, @RequestBody RoleRequest roleRequest){
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setMessage("Role updated");
        response.setResult(roleService.updateRole(id, roleRequest));
        return response;
    }

    @GetMapping
    public ApiResponse<Set<RoleResponse>> getRoles(){
        ApiResponse<Set<RoleResponse>> response = new ApiResponse<>();
        response.setMessage("Roles");
        response.setResult(roleService.getRoles());
        return response;
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable String id){
        roleService.deleteRole(id);
    }
}
