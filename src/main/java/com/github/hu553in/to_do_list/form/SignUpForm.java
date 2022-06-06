package com.github.hu553in.to_do_list.form;

import javax.validation.constraints.NotBlank;

public record SignUpForm(@NotBlank String username, @NotBlank String password) {
}
