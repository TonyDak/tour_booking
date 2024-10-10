package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

}