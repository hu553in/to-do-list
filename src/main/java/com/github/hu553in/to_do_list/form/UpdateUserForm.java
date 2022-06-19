package com.github.hu553in.to_do_list.form;

import com.github.hu553in.to_do_list.constraint.NullOrNotBlank;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public record UpdateUserForm(@Email String email,
                             @NullOrNotBlank @Size(min = 8, max = 255) String password,
                             Boolean admin) {
}
