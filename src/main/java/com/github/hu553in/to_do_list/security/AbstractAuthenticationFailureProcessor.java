package com.github.hu553in.to_do_list.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hu553in.to_do_list.exception.JwtAuthenticationException;
import com.github.hu553in.to_do_list.exception.JwtValidationException;
import com.github.hu553in.to_do_list.view.ApiErrorView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AbstractAuthenticationFailureProcessor {

    private final ObjectMapper objectMapper;

    protected void process(final HttpServletResponse response,
                           final AuthenticationException e) throws IOException {
        logger.error("Authentication is failed", e);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        List<String> details = new ArrayList<>();
        if (e instanceof JwtAuthenticationException) {
            StringBuilder stringBuilder = new StringBuilder();
            Optional
                    .ofNullable(e.getMessage())
                    .filter(StringUtils::hasLength)
                    .ifPresent(stringBuilder::append);
            Throwable cause = e.getCause();
            if (cause instanceof JwtValidationException) {
                Optional
                        .ofNullable(cause.getMessage())
                        .filter(StringUtils::hasLength)
                        .ifPresent(it -> stringBuilder.append(": ").append(it));
            }
            details.add(stringBuilder.toString());
        }
        ApiErrorView apiErrorView = ApiErrorView
                .builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message("User is not authorized to access resource")
                .details(details)
                .build();
        objectMapper.writeValue(response.getOutputStream(), apiErrorView);
    }

}
