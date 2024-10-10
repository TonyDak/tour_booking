package com.tourbooking.tour_booking.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tourbooking.tour_booking.dto.auth.AuthenticationResponse;
import com.tourbooking.tour_booking.dto.auth.AuthenticatonRequest;
import com.tourbooking.tour_booking.dto.auth.IntrospectRequest;
import com.tourbooking.tour_booking.dto.auth.IntrospectResponse;
import com.tourbooking.tour_booking.entity.User;
import com.tourbooking.tour_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    @NonFinal
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final PasswordEncoder passwordEncoder;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SECRET_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = (Date) signedJWT.getJWTClaimsSet().getClaim("exp");
        var verified = signedJWT.verify(jwsVerifier);
        return IntrospectResponse.builder().valid(verified && expirationTime.after(new Date())).build();

    }

    public AuthenticationResponse isAuthenticated(AuthenticatonRequest authenticatonRequest) {
        var user = userRepository.findByEmail(authenticatonRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        boolean authenticated =  passwordEncoder.matches(authenticatonRequest.getPassword(), user.getPassword());
        if(!authenticated){
            throw new RuntimeException("Invalid password");
        }
        var token = generateToken(user);
        return  AuthenticationResponse.builder().authenticated(authenticated).token(token).build();
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("tour-booking")
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + 1000 * 60 * 60 * 10))
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
