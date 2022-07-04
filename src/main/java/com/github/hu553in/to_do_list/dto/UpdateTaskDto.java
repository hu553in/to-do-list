package com.github.hu553in.to_do_list.dto;

import com.github.hu553in.to_do_list.enumeration.TaskStatus;

public record UpdateTaskDto(String text, TaskStatus status) {
}
