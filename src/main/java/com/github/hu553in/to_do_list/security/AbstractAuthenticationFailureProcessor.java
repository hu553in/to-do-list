package com.github.hu553in.to_do_list.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hu553in.to_do_list.exception.JwtAuthenticationException;
import com.github.hu553in.to_do_list.view.ApiErrorView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AbstractAuthenticationFailureProcessor {

    private final ObjectMapper objectMapper;

    protected void process(final HttpServletResponse response,
                           final AuthenticationException e) throws IOException {
        logger.error("Authentication failure", e);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        List<String> details = e instanceof JwtAuthenticationException
                ? List.of(e.getMessage())
                : List.of();
        ApiErrorView apiErrorView = ApiErrorView
                .builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message("User is not authorized to access resource")
                .details(details)
                .build();
        objectMapper.writeValue(response.getOutputStream(), apiErrorView);
    }

}
