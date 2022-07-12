package com.github.hu553in.to_do_list.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hu553in.to_do_list.view.ApiErrorView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimitErrorResponseBodyCustomizerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(request, response);
        if (response.getStatus() == HttpStatus.TOO_MANY_REQUESTS.value()) {
            logger.info(
                "Customizing rate limit error response body for {} {}",
                request.getMethod(),
                request.getRequestURI());
            ApiErrorView apiErrorView = ApiErrorView
                .builder()
                .message("Too many requests")
                .details(List.of())
                .build();
            objectMapper.writeValue(response.getWriter(), apiErrorView);
        }
    }

}
