package com.github.hu553in.to_do_list.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record SignUpForm(
        @NotNull @Email String email,
        @NotBlank @Size(min = 8, max = 255) String password) {
}
