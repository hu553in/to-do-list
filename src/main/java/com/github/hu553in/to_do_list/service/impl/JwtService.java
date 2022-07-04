package com.github.hu553in.to_do_list.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.hu553in.to_do_list.config.JwtConfiguration;
import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.entity.UserEntity;
import com.github.hu553in.to_do_list.enumeration.Authority;
import com.github.hu553in.to_do_list.exception.JwtValidationException;
import com.github.hu553in.to_do_list.exception.NotFoundException;
import com.github.hu553in.to_do_list.repository.jpa.UserRepository;
import com.github.hu553in.to_do_list.security.AuthenticatedUser;
import com.github.hu553in.to_do_list.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class JwtService implements IJwtService {

    private final JwtConfiguration jwtConfiguration;
    private final UserRepository userRepository;

    @Override
    public String buildJwt(final UserDto user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtConfiguration.getExpiresIn());
        Algorithm algorithm = Algorithm.HMAC512(jwtConfiguration.getSigningKey());
        return JWT
                .create()
                .withIssuer(jwtConfiguration.getIssuer())
                .withIssuedAt(now)
                .withNotBefore(now)
                .withSubject(String.valueOf(user.id()))
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    @Override
    public Authentication authenticateJwt(final String jwt) {
        DecodedJWT decodedJwt = decodeAndVerifyJwt(jwt);
        int userId;
        try {
            userId = Integer.parseInt(decodedJwt.getSubject());
        } catch (NumberFormatException e) {
            throw new JwtValidationException("JWT subject must be parseable integer representing user ID");
        }
        return authenticateUserById(userId);
    }

    private DecodedJWT decodeAndVerifyJwt(final String jwt) {
        Algorithm algorithm = Algorithm.HMAC512(jwtConfiguration.getSigningKey());
        try {
            return JWT
                    .require(algorithm)
                    .withIssuer(jwtConfiguration.getIssuer())
                    .withClaimPresence(RegisteredClaims.SUBJECT)
                    .withClaimPresence(RegisteredClaims.ISSUED_AT)
                    .withClaimPresence(RegisteredClaims.NOT_BEFORE)
                    .withClaimPresence(RegisteredClaims.EXPIRES_AT)
                    .acceptLeeway(jwtConfiguration.getLeewaySeconds())
                    .build()
                    .verify(jwt);
        } catch (JWTVerificationException e) {
            String message = e.getMessage();
            throw message != null
                    ? new JwtValidationException(message)
                    : new JwtValidationException();
        }
    }

    private Authentication authenticateUserById(final Integer userId) {
        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found by ID"));
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(Authority.ROLE_USER::toString);
        if (user.getAdmin()) {
            authorities.add(Authority.ROLE_ADMIN::toString);
        }
        return new AuthenticatedUser(userId, authorities);
    }

}
