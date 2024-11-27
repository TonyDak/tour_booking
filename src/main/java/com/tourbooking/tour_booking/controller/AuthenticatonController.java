package com.tourbooking.tour_booking.controller;

import com.nimbusds.jose.JOSEException;
import com.tourbooking.tour_booking.dto.ApiResponse;
import com.tourbooking.tour_booking.dto.auth.*;
import com.tourbooking.tour_booking.dto.auth.RegisterRequest;
import com.tourbooking.tour_booking.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login-google")
    public ApiResponse<AuthenticationResponse> handleGoogleLogin(OAuth2AuthenticationToken authentication) throws MessagingException {
        var authenticated = authenticationService.handleGoogleLogin(authentication);
        return ApiResponse.<AuthenticationResponse>builder().result(authenticated).build();
    }

    @PostMapping("/register")
    public ApiResponse<RegisterRequest> createUser(@RequestBody @Valid RegisterRequest registerRequest){
        ApiResponse<RegisterRequest> response = new ApiResponse<>();
        response.setMessage("User created");
        response.setResult(authenticationService.register(registerRequest));
        return response;
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> token(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var valid = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(valid).build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        return ApiResponse.<String>builder().result("Logged out").build();
    }

}
