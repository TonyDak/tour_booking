package com.tourbooking.tour_booking.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tourbooking.tour_booking.dto.auth.*;
import com.tourbooking.tour_booking.dto.auth.RegisterRequest;
import com.tourbooking.tour_booking.entity.InvalidatedToken;
import com.tourbooking.tour_booking.entity.User;
import com.tourbooking.tour_booking.mapper.UserMapper;
import com.tourbooking.tour_booking.repository.InvalidatedTokenRepository;
import com.tourbooking.tour_booking.repository.RoleRepository;
import com.tourbooking.tour_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (ParseException | JOSEException e) {
            log.error("Error introspecting token", e);
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }
    public RegisterRequest register(RegisterRequest registerRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = userMapper.toUser(registerRequest);

        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        var roles = roleRepository.findAllById(new HashSet<>(Set.of("USER")));
        user.setRoles(new HashSet<>(roles));
        user.setStatus(1);
        userRepository.save(user);
        return registerRequest;
    }
    public AuthenticationResponse isAuthenticated(AuthenticatonRequest authenticatonRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByEmail(authenticatonRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        boolean authenticated =  passwordEncoder.matches(authenticatonRequest.getPassword(), user.getPassword());
        if(!authenticated){
            throw new RuntimeException("Invalid password");
        }
        //check status
        if(user.getStatus() != 1){
            throw new RuntimeException("User is not active");
        }

        List<String> bookmarked = user.getBookMarks().stream()
                .map(bookmark -> bookmark.getTour().getId())
                .collect(Collectors.toList());
        var token = generateToken(user);
        return  AuthenticationResponse.builder().authenticated(authenticated).token(token).bookmarked(bookmarked).build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken());

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (ParseException | JOSEException e) {
            log.error("Error invalidating token", e);
            throw e;
        }
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("tour-booking")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error signing token", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!verified || new Date().after(expiryTime)) {
            throw new RuntimeException("Token is not valid");
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new RuntimeException("Token is invalidated");
        }
        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }
}
