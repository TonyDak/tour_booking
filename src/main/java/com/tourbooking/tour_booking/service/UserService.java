package com.tourbooking.tour_booking.service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.tourbooking.tour_booking.dto.user.ChangePasswordRequest;
import com.tourbooking.tour_booking.dto.user.UserCreate;
import com.tourbooking.tour_booking.dto.user.UserInfoUpdate;
import com.tourbooking.tour_booking.mapper.UserMapper;
import com.tourbooking.tour_booking.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tourbooking.tour_booking.entity.User;
import com.tourbooking.tour_booking.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public List<UserInfoUpdate> getUsers(){
        List<UserInfoUpdate> users;
        users = userRepository.findAll().stream()
                .map(userMapper::toUserInfoUpdate)
                .collect(Collectors.toList());
        return users;
    }

    public UserInfoUpdate getUser(String id) {
        User user = userRepository.findById(id).orElseThrow();
        return userMapper.toUserInfoUpdate(user);
    }

    public UserInfoUpdate getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        return userMapper.toUserInfoUpdate(user);
    }

    public UserCreate createUser(UserCreate userCreate) {

        if (userRepository.existsByEmail(userCreate.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = userMapper.toUser(userCreate);

        user.setPassword(passwordEncoder.encode(userCreate.getPassword()));
        var roles = roleRepository.findAllById(new HashSet<>(Set.of("USER")));
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        return userCreate;
    }
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserInfoUpdate updateUser(String id, UserInfoUpdate userInfo) {
        User user = userRepository.findById(id).orElseThrow();
        userMapper.updateUserFromDto(userInfo, user);
        var roles = roleRepository.findAllById(userInfo.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()));
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        return userInfo;
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

    }
}
