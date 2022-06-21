package com.github.hu553in.to_do_list.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@SecurityScheme(
        name = BearerJwtAuthSecurityScheme.NAME,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BearerJwtAuthSecurityScheme {

    String NAME = "BearerJwtAuth";

}
