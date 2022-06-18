package com.github.hu553in.to_do_list.security;

import com.github.hu553in.to_do_list.exception.JwtAuthenticationException;
import com.github.hu553in.to_do_list.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final IJwtService jwtService;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getCredentials());
        try {
            return jwtService.authenticateToken(token);
        } catch (Exception e) {
            throw new JwtAuthenticationException("Invalid JWT", e);
        }
    }

    @Override
    public boolean supports(final Class<?> authenticationClass) {
        return JwtToAuthenticate.class.isAssignableFrom(authenticationClass);
    }

}
