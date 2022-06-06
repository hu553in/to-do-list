package com.github.hu553in.to_do_list.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
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
    private Instant createdAt;
    private Instant updatedAt;

    @JsonGetter("createdAt")
    public String getCreatedAt() {
        return createdAt.toString();
    }

    @JsonGetter("updatedAt")
    public String getUpdatedAt() {
        return updatedAt.toString();
    }

}
