package com.github.hu553in.to_do_list.security;

import com.github.hu553in.to_do_list.exception.JwtAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class HeaderJwtAuthProcessingFilter extends AbstractJwtAuthProcessingFilter {

    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    public HeaderJwtAuthProcessingFilter(final RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    protected String getRawToken(final HttpServletRequest request) throws AuthenticationException {
        String header = request.getHeader(AUTH_HEADER_NAME);
        if (header.startsWith(AUTH_HEADER_PREFIX)) {
            String token = header.substring(AUTH_HEADER_PREFIX.length());
            if (token.trim().length() > 0) {
                return token;
            }
        }
        throw new JwtAuthenticationException("Invalid " + AUTH_HEADER_NAME + " header: " + header);
    }

}
