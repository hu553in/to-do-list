package com.github.hu553in.to_do_list.exception;

import java.io.Serial;

public class SortPropertyNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2647114225906799143L;

    public SortPropertyNotFoundException(final Throwable cause) {
        super(cause);
    }

}
