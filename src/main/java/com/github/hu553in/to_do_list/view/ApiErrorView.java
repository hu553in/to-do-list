package com.github.hu553in.to_do_list.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Collection;

@Getter
@Builder
@Schema(requiredProperties = {"timestamp", "message", "details"})
public class ApiErrorView {

    @JsonFormat(shape = Shape.STRING)
    private final Instant timestamp = Instant.now();

    private String message;
    private Collection<String> details;

}
