package com.github.hu553in.to_do_list.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateTaskForm(@NotBlank @Size(max = 255) String text) {
}
