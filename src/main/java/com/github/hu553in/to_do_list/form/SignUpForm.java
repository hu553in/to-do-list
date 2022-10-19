package com.github.hu553in.to_do_list.form;

import com.github.hu553in.to_do_list.constraint.Password;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record SignUpForm(@Schema(format = "email") @NotNull @Email String email,
                         @Password String password) {
}
