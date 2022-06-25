package com.github.hu553in.to_do_list.config;

import com.github.hu553in.to_do_list.enumeration.Authority;
import com.github.hu553in.to_do_list.security.AuthenticationFailureEntryPoint;
import com.github.hu553in.to_do_list.security.CustomAccessDeniedHandler;
import com.github.hu553in.to_do_list.security.CustomAuthenticationFailureHandler;
import com.github.hu553in.to_do_list.security.HeaderJwtAuthenticationProcessingFilter;
import com.github.hu553in.to_do_list.security.JwtAuthenticationProvider;
import com.github.hu553in.to_do_list.service.IJwtService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private static final String[] PATTERNS_WITHOUT_AUTH = new String[]{
            "/sign-in",
            "/sign-up",

            // API docs
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    private static final List<RequestMatcher> REQUEST_MATCHERS_WITHOUT_AUTH = Arrays
            .stream(PATTERNS_WITHOUT_AUTH)
            .map(AntPathRequestMatcher::new)
            .collect(Collectors.toList());

    private final IJwtService jwtService;
    private final AuthenticationEntryPoint authenticationFailureEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public WebSecurityConfiguration(
            final IJwtService jwtService,
            @Qualifier(CustomAccessDeniedHandler.QUALIFIER) final AccessDeniedHandler accessDeniedHandler,
            // these abbreviations are used to shorten line length
            @Qualifier(AuthenticationFailureEntryPoint.QUALIFIER) final AuthenticationEntryPoint aep,
            @Qualifier(CustomAuthenticationFailureHandler.QUALIFIER) final AuthenticationFailureHandler afh) {
        this.jwtService = jwtService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationFailureEntryPoint = aep;
        this.authenticationFailureHandler = afh;
    }

    @Bean
    @SuppressFBWarnings(value = "THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION",
            justification = "Exception is thrown by Spring Security methods.")
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        AbstractAuthenticationProcessingFilter authProcessingFilter = new HeaderJwtAuthenticationProcessingFilter(
                new NegatedRequestMatcher(new AndRequestMatcher(REQUEST_MATCHERS_WITHOUT_AUTH)));
        authProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return httpSecurity
                .cors().and()
                .anonymous().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .requestCache().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).disable()
                .addFilterBefore(authProcessingFilter, FilterSecurityInterceptor.class)
                .authenticationProvider(new JwtAuthenticationProvider(jwtService))
                .authorizeRequests(it -> {
                    it.antMatchers(PATTERNS_WITHOUT_AUTH).permitAll();
                    it.antMatchers("/admin/**").hasAuthority(Authority.ROLE_ADMIN.toString());
                    it.anyRequest().authenticated();
                })
                .exceptionHandling(it -> {
                    it.authenticationEntryPoint(authenticationFailureEntryPoint);
                    it.accessDeniedHandler(accessDeniedHandler);
                })
                .build();
    }

}
