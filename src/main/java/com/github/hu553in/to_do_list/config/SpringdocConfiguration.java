package com.github.hu553in.to_do_list.config;

import com.github.hu553in.to_do_list.view.ApiErrorView;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class SpringdocConfiguration {

    @Bean
    public OpenApiCustomiser openApiCustomizer() {
        return this::addGlobalErrorApiResponses;
    }

    private void addGlobalErrorApiResponses(final OpenAPI openApi) {
        String forbiddenStatusCode = String.valueOf(HttpStatus.FORBIDDEN.value());
        ApiResponse forbiddenApiResponse = getErrorApiResponse(HttpStatus.FORBIDDEN.getReasonPhrase());
        String unauthorizedStatusCode = String.valueOf(HttpStatus.UNAUTHORIZED.value());
        ApiResponse unauthorizedApiResponse = getErrorApiResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        openApi
                .getPaths()
                .values()
                .forEach(pathItem -> pathItem
                        .readOperations()
                        .forEach(operation -> {
                            if (!CollectionUtils.isEmpty(operation.getSecurity())) {
                                operation
                                        .getResponses()
                                        .addApiResponse(forbiddenStatusCode, forbiddenApiResponse)
                                        .addApiResponse(unauthorizedStatusCode, unauthorizedApiResponse);
                            }
                        }));
    }

    private ApiResponse getErrorApiResponse(final String description) {
        Schema<ApiErrorView> schema = new Schema<>();
        schema.set$ref("#/components/schemas/" + ApiErrorView.class.getSimpleName());
        MediaType mediaType = new MediaType()
                .schema(schema);
        Content content = new Content()
                .addMediaType(APPLICATION_JSON_VALUE, mediaType);
        return new ApiResponse()
                .description(description)
                .content(content);
    }

}
