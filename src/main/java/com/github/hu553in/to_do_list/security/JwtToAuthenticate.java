package com.github.hu553in.to_do_list.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.io.Serial;

public class JwtToAuthenticate extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 7760793506377785329L;

    private final String token;

    public JwtToAuthenticate(final String token) {
        super(null);
        this.token = token;
        super.setAuthenticated(false);
    }

    @Override
    public void setAuthenticated(final boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("JWT to authenticate can not be marked as authenticated");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}
