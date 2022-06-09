package com.github.hu553in.to_do_list.test_util;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractDatabaseContainerTest {

    private static final String IMAGE_NAME = "postgres:14-alpine";

    @Container
    private static final JdbcDatabaseContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(IMAGE_NAME);

    @DynamicPropertySource
    static void setDynamicProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
    }

}
