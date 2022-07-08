package com.github.hu553in.to_do_list.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Qualifier(AuthenticationFailureEntryPoint.QUALIFIER)
@Slf4j
public class AuthenticationFailureEntryPoint extends AbstractAuthenticationFailureProcessor
    implements AuthenticationEntryPoint {

    public static final String QUALIFIER = "authenticationFailureEntryPoint";

    public AuthenticationFailureEntryPoint(final ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException e) throws IOException {
        process(response, e);
    }

}
