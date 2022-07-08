package com.github.hu553in.to_do_list.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Qualifier(CustomAuthenticationFailureHandler.QUALIFIER)
@Slf4j
public class CustomAuthenticationFailureHandler extends AbstractAuthenticationFailureProcessor
    implements AuthenticationFailureHandler {

    public static final String QUALIFIER = "customAuthenticationFailureHandler";

    public CustomAuthenticationFailureHandler(final ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final AuthenticationException e) throws IOException {
        process(response, e);
    }

}
