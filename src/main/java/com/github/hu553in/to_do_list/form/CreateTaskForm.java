package com.github.hu553in.to_do_list.form;

import javax.validation.constraints.NotBlank;

public record CreateTaskForm(@NotBlank String text) {
}
