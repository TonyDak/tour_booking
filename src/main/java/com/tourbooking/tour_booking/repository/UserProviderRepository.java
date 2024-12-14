package com.tourbooking.tour_booking.repository;

import com.tourbooking.tour_booking.entity.User;
import com.tourbooking.tour_booking.entity.UserProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProviderRepository extends JpaRepository<UserProvider, String> {
    Optional<UserProvider> findByProvider(UserProvider.Provider provider);
    Optional<UserProvider> findByUserAndProvider(User user, UserProvider.Provider provider);
}