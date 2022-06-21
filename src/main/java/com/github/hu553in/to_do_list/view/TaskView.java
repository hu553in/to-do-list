package com.github.hu553in.to_do_list.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.github.hu553in.to_do_list.enumeration.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(requiredProperties = {"id", "text", "status", "createdAt", "updatedAt"})
public record TaskView(Integer id,
                       String text,
                       TaskStatus status,
                       @JsonFormat(shape = Shape.STRING) Instant createdAt,
                       @JsonFormat(shape = Shape.STRING) Instant updatedAt) {
}
