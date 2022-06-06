package com.github.hu553in.to_do_list.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

public class AuthenticatedJwtToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 114342993139482413L;

    private final int subject;

    public AuthenticatedJwtToken(final int subject,
                                 final String details,
                                 final Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.subject = subject;
        setDetails(details);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return subject;
    }

}
