package com.github.hu553in.to_do_list.config;

import com.github.hu553in.to_do_list.view.ApiErrorView;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class SpringdocConfiguration {

    private static final Content ERROR_API_RESPONSE_CONTENT;

    static {
        Schema<ApiErrorView> schema = new Schema<>();
        schema.set$ref("#/components/schemas/" + ApiErrorView.class.getSimpleName());
        MediaType mediaType = new MediaType()
            .schema(schema);
        ERROR_API_RESPONSE_CONTENT = new Content()
            .addMediaType(APPLICATION_JSON_VALUE, mediaType);
    }

    @Bean
    public OpenApiCustomiser openApiCustomizer() {
        return this::addGlobalErrorApiResponses;
    }

    private void addGlobalErrorApiResponses(final OpenAPI openApi) {
        Map<String, ApiResponse> errorApiResponseForStatusCode = Map.of(
            String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            getErrorApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),
            String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()),
            getErrorApiResponse(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase()));
        Map<String, ApiResponse> authErrorApiResponseForStatusCode = Map.of(
            String.valueOf(HttpStatus.UNAUTHORIZED.value()),
            getErrorApiResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase()),
            String.valueOf(HttpStatus.FORBIDDEN.value()),
            getErrorApiResponse(HttpStatus.FORBIDDEN.getReasonPhrase()));
        openApi
            .getPaths()
            .values()
            .forEach(pathItem -> pathItem
                .readOperations()
                .forEach(operation -> {
                    ApiResponses responses = operation.getResponses();
                    errorApiResponseForStatusCode.forEach(responses::addApiResponse);
                    if (!CollectionUtils.isEmpty(operation.getSecurity())) {
                        authErrorApiResponseForStatusCode.forEach(responses::addApiResponse);
                    }
                }));
    }

    private ApiResponse getErrorApiResponse(final String description) {
        return new ApiResponse()
            .description(description)
            .content(ERROR_API_RESPONSE_CONTENT);
    }

}
