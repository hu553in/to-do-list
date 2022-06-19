package com.github.hu553in.to_do_list.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.hu553in.to_do_list.config.JwtConfiguration;
import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.entity.UserEntity;
import com.github.hu553in.to_do_list.exception.JwtValidationException;
import com.github.hu553in.to_do_list.exception.NotFoundException;
import com.github.hu553in.to_do_list.model.Authority;
import com.github.hu553in.to_do_list.repository.jpa.UserRepository;
import com.github.hu553in.to_do_list.security.AuthenticatedUser;
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
        Algorithm algorithm = Algorithm.HMAC512(jwtConfiguration.getSigningKey());
        return JWT
                .create()
                .withIssuer(jwtConfiguration.getIssuer())
                .withIssuedAt(currentDate)
                .withNotBefore(currentDate)
                .withSubject(String.valueOf(user.id()))
                .withExpiresAt(expirationDate)
                .sign(algorithm);
    }

    @Override
    public Authentication authenticateToken(final String token) {
        Algorithm algorithm = Algorithm.HMAC512(jwtConfiguration.getSigningKey());
        DecodedJWT decodedJwt;
        try {
            decodedJwt = JWT
                    .require(algorithm)
                    .withIssuer(jwtConfiguration.getIssuer())
                    .withClaimPresence(PublicClaims.SUBJECT)
                    .acceptLeeway(jwtConfiguration.getLeewaySeconds())
                    .build()
                    .verify(token);
        } catch (InvalidClaimException | TokenExpiredException e) {
            String message = e.getMessage();
            throw message != null
                    ? new JwtValidationException(message)
                    : new JwtValidationException();
        }
        validateDateClaimsNotNull(decodedJwt);
        Integer userId;
        try {
            userId = Integer.valueOf(decodedJwt.getSubject());
        } catch (NumberFormatException e) {
            throw new JwtValidationException("JWT subject must be parseable integer representing user ID");
        }
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

    // TODO: probably this should be changed in future, see https://github.com/auth0/java-jwt/issues/558
    private void validateDateClaimsNotNull(final DecodedJWT decodedJWT) {
        validateClaimNotNull(decodedJWT.getIssuedAt(), PublicClaims.ISSUED_AT);
        validateClaimNotNull(decodedJWT.getNotBefore(), PublicClaims.NOT_BEFORE);
        validateClaimNotNull(decodedJWT.getExpiresAt(), PublicClaims.EXPIRES_AT);
    }

    private void validateClaimNotNull(final Object claim, final String claimName) {
        if (claim == null) {
            throw new JwtValidationException("JWT " + claimName + " claim must be not null");
        }
    }

}
