package com.tourbooking.tour_booking.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tourbooking.tour_booking.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    //find user by token
    Optional<User> findByToken(String token);

    Page<User> findAll(Pageable pageable);

    //find by user name
    @Query("SELECT u FROM User u WHERE LOWER(u.user_name) LIKE LOWER(CONCAT('%', :user_name, '%'))")
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    //find by email
    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    //find by phone number
    @Query("SELECT u FROM User u WHERE LOWER(u.phone_number) LIKE LOWER(CONCAT('%', :phone_number, '%'))")
    Page<User> findByPhoneContainingIgnoreCase(String phone_number, Pageable pageable);
}
