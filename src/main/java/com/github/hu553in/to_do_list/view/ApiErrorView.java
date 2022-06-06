package com.github.hu553in.to_do_list.view;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class ApiErrorView {

    private final Instant timestamp = Instant.now();
    private HttpStatus status;
    private String message;
    private List<String> details;

    @JsonGetter("timestamp")
    public String getTimestamp() {
        return timestamp.toString();
    }

}
