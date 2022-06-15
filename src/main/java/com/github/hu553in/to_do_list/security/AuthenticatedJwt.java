package com.github.hu553in.to_do_list.security;

import com.github.hu553in.to_do_list.model.UserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

public class AuthenticatedJwt extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 114342993139482413L;

    private final String username;

    public AuthenticatedJwt(final String username,
                            final UserDetails userDetails,
                            final Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.username = username;
        setDetails(userDetails);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

}
