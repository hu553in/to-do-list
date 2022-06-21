package com.github.hu553in.to_do_list.view;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {"id", "email", "admin"})
public record UserView(Integer id,
                       @Schema(format = "email") String email,
                       Boolean admin) {
}
