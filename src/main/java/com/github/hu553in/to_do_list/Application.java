package com.github.hu553in.to_do_list;

import com.github.hu553in.to_do_list.swagger.BearerJwtAuthSecurityScheme;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
    title = "To-do list",
    description = "The backend part of an app for managing tasks. "
                  + "Operations with [ADMIN] description prefix are available only for users with admin rights.",
    contact = @Contact(
        name = "Ruslan Khasanshin",
        url = "https://hu553in.notion.site",
        email = "r.m.khasanshin@gmail.com"),
    version = "1.0.0"))
@BearerJwtAuthSecurityScheme
@SpringBootApplication
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
