package com.tourbooking.tour_booking.config;

import com.tourbooking.tour_booking.entity.User;
import com.tourbooking.tour_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
//                var role = new HashSet<String>();
//                role.add(Role.ADMIN.name());

                User user = User.builder()
                        .email("admin@admin.com")
                        .password(passwordEncoder.encode("admin"))
                        //.role(Role.ADMIN)
                        .build();

                userRepository.save(user);
                log.warn("Admin user created");
            }

        };
    }
}
