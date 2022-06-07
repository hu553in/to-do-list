package com.github.hu553in.to_do_list.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Collection;

@Getter
@Builder
public class ApiErrorView {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final Instant timestamp = Instant.now();

    private HttpStatus status;
    private String message;
    private Collection<String> details;

}
