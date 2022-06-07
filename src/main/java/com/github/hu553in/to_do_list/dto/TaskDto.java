package com.github.hu553in.to_do_list.dto;

import com.github.hu553in.to_do_list.model.TaskStatus;

import java.time.Instant;

public record TaskDto (Integer id, String text, TaskStatus status, Instant createdAt, Instant updatedAt) {
}
