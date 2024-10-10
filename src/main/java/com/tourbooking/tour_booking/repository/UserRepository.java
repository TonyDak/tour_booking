package com.tourbooking.tour_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tourbooking.tour_booking.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
