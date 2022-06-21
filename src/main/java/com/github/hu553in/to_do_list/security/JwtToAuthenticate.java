package com.github.hu553in.to_do_list.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.io.Serial;

public class JwtToAuthenticate extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 7760793506377785329L;

    private final String jwt;

    public JwtToAuthenticate(final String jwt) {
        super(null);
        this.jwt = jwt;
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
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}
