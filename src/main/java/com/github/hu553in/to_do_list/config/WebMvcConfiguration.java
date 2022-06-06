package com.github.hu553in.to_do_list.config;

import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebMvc
@Setter
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull final CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods(GET.toString(), POST.toString(), PATCH.toString(), DELETE.toString());
    }

}
