package com.github.hu553in.to_do_list.exception;

import java.io.Serial;

public class JwtValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3619037478636250121L;

    public JwtValidationException() {
        super();
    }

    public JwtValidationException(final String message) {
        super(message);
    }

}
