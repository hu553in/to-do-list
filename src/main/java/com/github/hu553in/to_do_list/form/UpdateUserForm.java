package com.github.hu553in.to_do_list.form;

import com.github.hu553in.to_do_list.constraint.NullOrNotBlankString;

public record UpdateUserForm(@NullOrNotBlankString String username,
                             @NullOrNotBlankString String password,
                             Boolean isAdmin) {
}
