package com.github.hu553in.to_do_list.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hu553in.to_do_list.view.ApiErrorView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Qualifier(CustomAccessDeniedHandler.QUALIFIER)
@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    public static final String QUALIFIER = "customAccessDeniedHandler";

    private final ObjectMapper objectMapper;

    @Override
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AccessDeniedException e) throws IOException {
        logger.error("Access is denied", e);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ApiErrorView apiErrorView = ApiErrorView
                .builder()
                .message("User does not have access to resource")
                .details(List.of())
                .build();
        objectMapper.writeValue(response.getOutputStream(), apiErrorView);
    }

}
