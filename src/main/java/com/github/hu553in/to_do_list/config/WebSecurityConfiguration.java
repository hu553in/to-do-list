package com.github.hu553in.to_do_list.config;

import com.github.hu553in.to_do_list.model.Authority;
import com.github.hu553in.to_do_list.security.AuthenticationFailureEntryPoint;
import com.github.hu553in.to_do_list.security.CustomAccessDeniedHandler;
import com.github.hu553in.to_do_list.security.CustomAuthenticationFailureHandler;
import com.github.hu553in.to_do_list.security.HeaderJwtAuthProcessingFilter;
import com.github.hu553in.to_do_list.security.JwtAuthenticationProvider;
import com.github.hu553in.to_do_list.service.IJwtService;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private static final String[] ANT_PATTERNS_TO_PERMIT_ALL = new String[]{
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
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public WebSecurityConfiguration(
            final IJwtService jwtService,
            // these abbreviations are used to shorten line length
            @Qualifier(AuthenticationFailureEntryPoint.QUALIFIER) final AuthenticationEntryPoint aep,
            @Qualifier(CustomAccessDeniedHandler.QUALIFIER) final AccessDeniedHandler adh,
            @Qualifier(CustomAuthenticationFailureHandler.QUALIFIER) final AuthenticationFailureHandler afh) {
        this.jwtService = jwtService;
        this.authenticationEntryPoint = aep;
        this.accessDeniedHandler = adh;
        this.authenticationFailureHandler = afh;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        RequestMatcher signInMatcher = new AntPathRequestMatcher("/sign-in");
        RequestMatcher nonSignInMatcher = new NegatedRequestMatcher(signInMatcher);
        // this abbreviation is used to shorten line length
        AbstractAuthenticationProcessingFilter apf = new HeaderJwtAuthProcessingFilter(nonSignInMatcher);
        apf.setAuthenticationFailureHandler(authenticationFailureHandler);
        return httpSecurity
                .cors().and()
                .anonymous().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .requestCache().disable()
                .sessionManagement(configurer -> {
                    configurer.sessionCreationPolicy(SessionCreationPolicy.NEVER);
                    configurer.disable();
                })
                .addFilterBefore(apf, FilterSecurityInterceptor.class)
                .authenticationProvider(new JwtAuthenticationProvider(jwtService))
                .authorizeRequests(configurer -> {
                    configurer.antMatchers(ANT_PATTERNS_TO_PERMIT_ALL).permitAll();
                    configurer.antMatchers("/user/**").hasAuthority(Authority.ROLE_ADMIN.toString());
                    configurer.anyRequest().authenticated();
                })
                .exceptionHandling(configurer -> {
                    configurer.authenticationEntryPoint(authenticationEntryPoint);
                    configurer.accessDeniedHandler(accessDeniedHandler);
                })
                .build();
    }

}
