package com.github.hu553in.to_do_list.form;

import com.github.hu553in.to_do_list.constraint.NullOrNotBlank;
import com.github.hu553in.to_do_list.model.TaskStatus;

import javax.validation.constraints.Size;

public record UpdateTaskForm(
        @NullOrNotBlank @Size(max = 255) String text,
        TaskStatus status) {
}
