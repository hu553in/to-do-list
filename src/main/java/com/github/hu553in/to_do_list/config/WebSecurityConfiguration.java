package com.github.hu553in.to_do_list.config;

import com.github.hu553in.to_do_list.model.Authority;
import com.github.hu553in.to_do_list.security.AuthenticationFailureEntryPoint;
import com.github.hu553in.to_do_list.security.CustomAccessDeniedHandler;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;

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

    public WebSecurityConfiguration(
            final IJwtService jwtService,
            @Qualifier(AuthenticationFailureEntryPoint.QUALIFIER) final AuthenticationEntryPoint authEntryPoint,
            @Qualifier(CustomAccessDeniedHandler.QUALIFIER) final AccessDeniedHandler accessDeniedHandler) {
        this.jwtService = jwtService;
        this.authenticationEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

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
                .antMatchers(ANT_PATTERNS_TO_PERMIT_ALL).permitAll()
                .antMatchers("/user/**").hasAuthority(Authority.ROLE_ADMIN.toString())
                .anyRequest().authenticated().and()
                .authenticationProvider(new JwtAuthenticationProvider(jwtService))
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        return httpSecurity.build();
    }

}
