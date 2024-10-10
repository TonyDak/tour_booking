package com.tourbooking.tour_booking.config;

import com.tourbooking.tour_booking.entity.Permission;
import com.tourbooking.tour_booking.entity.Role;
import com.tourbooking.tour_booking.entity.User;
import com.tourbooking.tour_booking.repository.RoleRepository;
import com.tourbooking.tour_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
                roleRepository.save(Role.builder()
                        .name("USER")
                        .description("User role")
                        .permissions(new HashSet<>(Set.of(Permission.READ)))
                        .build());

                Role roleAdmin = roleRepository.save(Role.builder()
                        .name("ADMIN")
                        .description("Admin role")
                        .permissions(new HashSet<>(Set.of(Permission.CREATE, Permission.READ, Permission.UPDATE, Permission.DELETE)))
                        .build());

                User user = User.builder()
                        .email("admin@admin.com")
                        .password(passwordEncoder.encode("admin"))
                        .roles(new HashSet<>(Set.of(roleAdmin)))
                        .build();

                userRepository.save(user);
                log.warn("Admin user created");
            }

        };
    }
}
