package com.github.hu553in.to_do_list.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.hu553in.to_do_list.config.JwtConfiguration;
import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.entity.UserEntity;
import com.github.hu553in.to_do_list.model.Authority;
import com.github.hu553in.to_do_list.repository.jpa.UserRepository;
import com.github.hu553in.to_do_list.security.AuthenticatedJwt;
import com.github.hu553in.to_do_list.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class JwtService implements IJwtService {

    private final JwtConfiguration jwtConfiguration;
    private final UserRepository userRepository;

    @Override
    public String buildToken(final UserDto user) {
        Instant now = Instant.now();
        Date currentDate = Date.from(now);
        Date expirationDate = Date.from(now.plus(jwtConfiguration.getExpiresIn()));
        return JWT
                .create()
                .withIssuer(jwtConfiguration.getIssuer())
                .withIssuedAt(currentDate)
                .withNotBefore(currentDate)
                .withSubject(user.username())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC512(jwtConfiguration.getSigningKey()));
    }

    @Override
    public Authentication authenticateToken(final String token) {
        DecodedJWT decodedJwt = JWT
                .require(Algorithm.HMAC512(jwtConfiguration.getSigningKey()))
                .withIssuer(jwtConfiguration.getIssuer())
                .withClaimPresence(PublicClaims.ISSUED_AT)
                .withClaimPresence(PublicClaims.NOT_BEFORE)
                .withClaimPresence(PublicClaims.SUBJECT)
                .withClaimPresence(PublicClaims.EXPIRES_AT)
                .acceptLeeway(jwtConfiguration.getLeewaySeconds())
                .build()
                .verify(token);
        String username = decodedJwt.getSubject();
        UserEntity user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Unable to find user by data from claims"));
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(Authority.ROLE_USER::toString);
        if (user.getIsAdmin()) {
            authorities.add(Authority.ROLE_ADMIN::toString);
        }
        return new AuthenticatedJwt(username, authorities);
    }

}
