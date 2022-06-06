package com.github.hu553in.to_do_list.config;

import com.github.hu553in.to_do_list.model.Authority;
import com.github.hu553in.to_do_list.security.HeaderJwtAuthProcessingFilter;
import com.github.hu553in.to_do_list.security.JwtAuthenticationProvider;
import com.github.hu553in.to_do_list.service.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private static final String[] antPatternsToPermit = new String[]{
            "/sign-in",
            "/sign-up",

            // API docs
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    private final IJwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        RequestMatcher signInMatcher = new AntPathRequestMatcher("/sign-in");
        RequestMatcher nonSignInMatcher = new NegatedRequestMatcher(signInMatcher);
        Filter authProcessingFilter = new HeaderJwtAuthProcessingFilter(nonSignInMatcher);
        httpSecurity
                .cors().and()
                .anonymous().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).disable()
                .requestCache().disable()
                .addFilterBefore(authProcessingFilter, FilterSecurityInterceptor.class)
                .authorizeRequests()
                .antMatchers(antPatternsToPermit).permitAll()
                .antMatchers("/user/**").hasAuthority(Authority.ROLE_ADMIN.toString())
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(new JwtAuthenticationProvider(jwtService));
        return httpSecurity.build();
    }

}
