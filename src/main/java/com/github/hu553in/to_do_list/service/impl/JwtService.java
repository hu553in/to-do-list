package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.config.JwtConfiguration;
import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.model.Authority;
import com.github.hu553in.to_do_list.model.UserDetails;
import com.github.hu553in.to_do_list.security.AuthenticatedJwt;
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

    private static final String ID_CLAIM_NAME = "sid";
    private static final String IS_ADMIN_CLAIM_NAME = "adm";

    private final JwtConfiguration jwtConfiguration;

    @Override
    public String create(final UserDto user) {
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
    public Authentication parse(final String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(jwtConfiguration.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
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

}
