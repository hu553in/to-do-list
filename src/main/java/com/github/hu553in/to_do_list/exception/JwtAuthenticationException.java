package com.github.hu553in.to_do_list.exception;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

public class JwtAuthenticationException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 2262761501796206378L;

    public JwtAuthenticationException(final String message) {
        super(message);
    }

    public JwtAuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
