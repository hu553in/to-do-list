package com.github.hu553in.to_do_list.form;

import com.github.hu553in.to_do_list.constraint.Password;

import javax.validation.constraints.Email;

public record UpdateUserForm(@Email String email,
                             @Password(nullable = true) String password,
                             Boolean admin) {
}
