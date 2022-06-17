package com.github.hu553in.to_do_list.exception;

import java.io.Serial;

public class ServerErrorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1924424106714728486L;

    public ServerErrorException(final String message) {
        super(message);
    }

}
