package com.github.hu553in.to_do_list.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConfigurationProperties(prefix = "cors")
@EnableWebMvc
@Setter
public class WebMvcConfiguration implements WebMvcConfigurer {

    private static final String DELIMITER = ",";

    private String allowedOriginPatterns = "*";
    private boolean allowCredentials = true;
    private String allowedMethods = "GET,POST,PATCH,DELETE";
    private String allowedHeaders = "Authorization," +
            "Origin," +
            "Accept," +
            "Key," +
            "DNT," +
            "Keep-Alive," +
            "User-Agent," +
            "X-Requested-With," +
            "If-Modified-Since," +
            "Cache-Control," +
            "Content-Type";

    @Override
    public void addCorsMappings(@NonNull final CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOriginPatterns(allowedOriginPatterns.split(DELIMITER))
                .allowCredentials(allowCredentials)
                .allowedMethods(allowedMethods.split(DELIMITER))
                .allowedHeaders(allowedHeaders.split(DELIMITER));
    }

}
