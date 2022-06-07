package com.github.hu553in.to_do_list.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.github.hu553in.to_do_list.model.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class TaskDto {

    private Integer id;
    private String text;
    private TaskStatus status;

    @JsonFormat(shape = Shape.STRING)
    private Instant createdAt;

    @JsonFormat(shape = Shape.STRING)
    private Instant updatedAt;

}
