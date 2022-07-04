package com.github.hu553in.to_do_list.security;

import com.github.hu553in.to_do_list.exception.JwtAuthenticationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;

public class HeaderJwtAuthenticationProcessingFilter extends AbstractJwtAuthenticationProcessingFilter {

    private static final String AUTHORIZATION_HEADER_PREFIX = TokenType.BEARER.getValue() + " ";

    public HeaderJwtAuthenticationProcessingFilter(final RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    protected String extractJwtFromRequest(final HttpServletRequest request) throws AuthenticationException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            String jwt = StringUtils
                    .substring(authorizationHeader, AUTHORIZATION_HEADER_PREFIX.length())
                    .trim();
            if (jwt.length() > 0) {
                return jwt;
            }
        }
        throw new JwtAuthenticationException(MessageFormat.format(
                "Invalid {0} header: {1}",
                HttpHeaders.AUTHORIZATION,
                authorizationHeader));
    }

}
