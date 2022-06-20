package com.github.hu553in.to_do_list.security;

import com.github.hu553in.to_do_list.enumeration.Authority;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.security.config.Elements.ANONYMOUS;

public abstract class AbstractJwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public AbstractJwtAuthenticationProcessingFilter(final RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response) throws AuthenticationException {
        try {
            return new JwtToAuthenticate(extractTokenFromRequest(request));
        } catch (Exception e) {
            return new AnonymousAuthenticationToken(
                    ANONYMOUS,
                    ANONYMOUS,
                    List.of(Authority.ROLE_ANONYMOUS::toString));
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain filterChain,
                                            final Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    protected abstract String extractTokenFromRequest(final HttpServletRequest request) throws AuthenticationException;

}
