package com.github.hu553in.to_do_list.security;

import com.github.hu553in.to_do_list.exception.JwtAuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;

public class HeaderJwtAuthenticationProcessingFilter extends AbstractJwtAuthenticationProcessingFilter {

    private static final String AUTHORIZATION_HEADER_PREFIX = TokenType.BEARER.getValue() + " ";

    public HeaderJwtAuthenticationProcessingFilter(final RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    protected String getRawToken(final HttpServletRequest request) throws AuthenticationException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            String token = StringUtils
                    .substring(header, AUTHORIZATION_HEADER_PREFIX.length() + 1)
                    .trim();
            if (token.length() > 0) {
                return token;
            }
        }
        throw new JwtAuthenticationException("Invalid " + HttpHeaders.AUTHORIZATION + " header: " + header);
    }

}
