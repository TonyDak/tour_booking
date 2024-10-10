package com.tourbooking.tour_booking.controller;

import com.nimbusds.jose.JOSEException;
import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.auth.*;
import com.tourbooking.tour_booking.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticatonController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticatonRequest request){
        var authenticated = authenticationService.isAuthenticated(request);
        return ApiResponse.<AuthenticationResponse>builder().result(authenticated).build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> login(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var valid = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(valid).build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        return ApiResponse.<String>builder().result("Logged out").build();
    }

}
