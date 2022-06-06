package com.github.hu553in.to_do_list.security;

import com.github.hu553in.to_do_list.exception.JwtAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeaderJwtAuthProcessingFilter extends AbstractJwtAuthProcessingFilter {

    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final Pattern TOKEN_PATTERN = Pattern.compile("^Bearer\\s+(.*)$");
    private static final int TOKEN_GROUP = 1;

    public HeaderJwtAuthProcessingFilter(final RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    protected String getToken(final HttpServletRequest request) throws AuthenticationException {
        String header = request.getHeader(AUTH_HEADER_NAME);
        Matcher matcher = TOKEN_PATTERN.matcher(header);
        if (matcher.matches()) {
            return matcher.group(TOKEN_GROUP);
        } else {
            throw new JwtAuthenticationException("Invalid " + AUTH_HEADER_NAME + " header: " + header);
        }
    }

}
