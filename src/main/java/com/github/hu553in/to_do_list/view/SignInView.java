package com.github.hu553in.to_do_list.view;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = "token")
public record SignInView(String token) {
}
