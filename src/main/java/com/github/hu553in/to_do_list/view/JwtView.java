package com.github.hu553in.to_do_list.view;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = "jwt")
public record JwtView(@Schema(format = "jwt") String jwt) {
}
