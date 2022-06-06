package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.config.JwtConfiguration;
import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.model.Authority;
import com.github.hu553in.to_do_list.security.AuthenticatedJwtToken;
import com.github.hu553in.to_do_list.service.IJwtService;
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

@Service
@RequiredArgsConstructor
public class JwtService implements IJwtService {

    private static final String USERNAME_CLAIM_NAME = "username";
    private static final String IS_ADMIN_CLAIM_NAME = "isAdmin";

    private final JwtConfiguration jwtConfiguration;

    @Override
    public String createToken(final UserDto restUser) {
        Instant now = Instant.now();
        Claims claims = Jwts
                .claims()
                .setIssuer(jwtConfiguration.getTokenIssuer())
                .setIssuedAt(Date.from(now))
                .setNotBefore(Date.from(now))
                .setSubject(restUser.getId().toString())
                .setExpiration(Date.from(now.plus(jwtConfiguration.getTokenExpiredIn())));
        claims.put(USERNAME_CLAIM_NAME, restUser.getUsername());
        claims.put(IS_ADMIN_CLAIM_NAME, restUser.getIsAdmin());
        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(jwtConfiguration.getTokenSigningKey()), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public Authentication parseToken(final String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(jwtConfiguration.getTokenSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        int id = Integer.parseInt(claims.getSubject());
        String username = claims.get(USERNAME_CLAIM_NAME, String.class);
        Boolean isAdmin = claims.get(IS_ADMIN_CLAIM_NAME, Boolean.class);
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(Authority.ROLE_USER::toString);
        if (isAdmin) {
            authorities.add(Authority.ROLE_ADMIN::toString);
        }
        return new AuthenticatedJwtToken(id, username, authorities);
    }

}
