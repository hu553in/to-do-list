package com.github.hu553in.to_do_list.form;

import com.github.hu553in.to_do_list.constraint.NullOrNotBlankString;
import com.github.hu553in.to_do_list.model.TaskStatus;

public record UpdateTaskForm(@NullOrNotBlankString String text, TaskStatus status) {
}
