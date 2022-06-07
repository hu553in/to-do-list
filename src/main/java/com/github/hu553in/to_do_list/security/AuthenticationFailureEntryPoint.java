package com.github.hu553in.to_do_list.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hu553in.to_do_list.view.ApiErrorView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Qualifier(AuthenticationFailureEntryPoint.QUALIFIER)
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFailureEntryPoint implements AuthenticationEntryPoint {

    public static final String QUALIFIER = "authenticationFailureEntryPoint";

    private final ObjectMapper objectMapper;

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException e) throws IOException {
        log.error("Authentication failure", e);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiErrorView apiErrorView = ApiErrorView
                .builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message("User is not authorized to access resource")
                .details(List.of())
                .build();
        objectMapper.writeValue(response.getOutputStream(), apiErrorView);
    }

}
