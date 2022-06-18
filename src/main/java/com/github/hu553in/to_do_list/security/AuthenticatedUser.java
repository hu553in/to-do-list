package com.github.hu553in.to_do_list.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

public class AuthenticatedUser extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 114342993139482413L;

    private final Integer id;

    public AuthenticatedUser(final Integer id, final Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.id = id;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return id;
    }

}
