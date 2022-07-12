package com.github.hu553in.to_do_list.config;

import com.github.hu553in.to_do_list.view.ApiErrorView;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class SpringdocConfiguration {

    private static final Content ERROR_API_RESPONSE_CONTENT = new Content()
        .addMediaType(APPLICATION_JSON_VALUE, new MediaType()
            .schema(new Schema<ApiErrorView>()
                .$ref("#/components/schemas/" + ApiErrorView.class.getSimpleName())));

    private static final ImmutablePair<String, Header> RATE_LIMIT_REMAINING_HEADER = new ImmutablePair<>(
        "X-Rate-Limit-Remaining",
        new Header()
            .description("The number of requests remaining until the rate limit is exceeded")
            .schema(new Schema<String>()
                .type("string")
                .example("5")));

    private static final ImmutablePair<String, Header> RATE_LIMIT_RETRY_AFTER_SECONDS_HEADER = new ImmutablePair<>(
        "X-Rate-Limit-Retry-After-Seconds",
        new Header()
            .description("The number of seconds after which the request can be repeated")
            .schema(new Schema<String>()
                .type("string")
                .example("60")));

    private static final Map<String, ApiResponse> ERROR_API_RESPONSE_FOR_STATUS_CODE = Map.of(
        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
        getErrorApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),
        String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()),
        getErrorApiResponse(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())
            .addHeaderObject(
                RATE_LIMIT_RETRY_AFTER_SECONDS_HEADER.left,
                RATE_LIMIT_RETRY_AFTER_SECONDS_HEADER.right));

    private static final Map<String, ApiResponse> AUTH_ERROR_API_RESPONSE_FOR_STATUS_CODE = Map.of(
        String.valueOf(HttpStatus.UNAUTHORIZED.value()),
        getErrorApiResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase()),
        String.valueOf(HttpStatus.FORBIDDEN.value()),
        getErrorApiResponse(HttpStatus.FORBIDDEN.getReasonPhrase()));

    private static ApiResponse getErrorApiResponse(final String description) {
        return new ApiResponse()
            .description(description)
            .content(ERROR_API_RESPONSE_CONTENT);
    }

    @Bean
    public OpenApiCustomiser openApiCustomizer() {
        return openApi -> openApi
            .getPaths()
            .values()
            .forEach(pathItem -> pathItem
                .readOperations()
                .forEach(operation -> {
                    ApiResponses responses = operation.getResponses();
                    ERROR_API_RESPONSE_FOR_STATUS_CODE.forEach(responses::addApiResponse);
                    if (!CollectionUtils.isEmpty(operation.getSecurity())) {
                        AUTH_ERROR_API_RESPONSE_FOR_STATUS_CODE.forEach(responses::addApiResponse);
                    }
                    responses.forEach((responseName, response) -> {
                        if (!Objects.equals(responseName, String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()))) {
                            response.addHeaderObject(
                                RATE_LIMIT_REMAINING_HEADER.left,
                                RATE_LIMIT_REMAINING_HEADER.right);
                        }
                    });
                }));
    }

}
