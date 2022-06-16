package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.config.JwtConfiguration;
import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.model.Authority;
import com.github.hu553in.to_do_list.model.UserDetails;
import com.github.hu553in.to_do_list.security.AuthenticatedJwt;
import com.github.hu553in.to_do_list.service.IJwtService;
import com.github.hu553in.to_do_list.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService implements IJwtService {

    private static final String ID_CLAIM_NAME = "id";
    private static final String IS_ADMIN_CLAIM_NAME = "is_admin";
    private static final Integer ALLOWED_CLOCK_SKEW_SECONDS = 180;

    private final JwtConfiguration jwtConfiguration;
    private final IUserService userService;

    @Override
    public String buildToken(final UserDto user) {
        Instant now = Instant.now();
        Date currentDate = Date.from(now);
        Date expirationDate = Date.from(now.plus(jwtConfiguration.getExpiresIn()));
        return Jwts
                .builder()
                .setIssuer(jwtConfiguration.getIssuer())
                .setIssuedAt(currentDate)
                .setNotBefore(currentDate)
                .setSubject(user.getUsername())
                .setExpiration(expirationDate)
                .claim(ID_CLAIM_NAME, user.getId())
                .claim(IS_ADMIN_CLAIM_NAME, user.getIsAdmin())
                .signWith(Keys.hmacShaKeyFor(jwtConfiguration.getSigningKey()), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public Authentication authenticateToken(final String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(jwtConfiguration.getSigningKey())
                .requireIssuer(jwtConfiguration.getIssuer())
                .setAllowedClockSkewSeconds(ALLOWED_CLOCK_SKEW_SECONDS)
                .build()
                .parseClaimsJws(token)
                .getBody();
        validateClaims(claims);
        String username = claims.getSubject();
        Integer id = claims.get(ID_CLAIM_NAME, Integer.class);
        UserDetails userDetails = new UserDetails(id);
        Boolean isAdmin = claims.get(IS_ADMIN_CLAIM_NAME, Boolean.class);
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(Authority.ROLE_USER::toString);
        if (isAdmin) {
            authorities.add(Authority.ROLE_ADMIN::toString);
        }
        return new AuthenticatedJwt(username, userDetails, authorities);
    }

    private void validateClaims(final Claims claims) {
        getNullableClaimOrElseThrowException(claims.getExpiration(), Claims.EXPIRATION);
        getNullableClaimOrElseThrowException(claims.getIssuedAt(), Claims.ISSUED_AT);
        getNullableClaimOrElseThrowException(claims.getNotBefore(), Claims.NOT_BEFORE);
        String username = getNullableClaimOrElseThrowException(
                claims.getSubject(),
                Claims.SUBJECT);
        Integer id = getNullableClaimOrElseThrowException(
                claims.get(ID_CLAIM_NAME, Integer.class),
                ID_CLAIM_NAME);
        Boolean isAdmin = getNullableClaimOrElseThrowException(
                claims.get(IS_ADMIN_CLAIM_NAME, Boolean.class),
                IS_ADMIN_CLAIM_NAME);
        if (!userService.existsByUsernameAndIdAndIsAdmin(username, id, isAdmin)) {
            throw new IllegalArgumentException("Unable to find user by data from claims");
        }
    }

    private <T> T getNullableClaimOrElseThrowException(final T claim, final String claimName) {
        return Optional
                .ofNullable(claim)
                .orElseThrow(() -> new IllegalArgumentException(claimName + " claim must be not null"));
    }

}
