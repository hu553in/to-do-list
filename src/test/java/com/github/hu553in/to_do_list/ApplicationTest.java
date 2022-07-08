package com.github.hu553in.to_do_list;

import com.github.hu553in.to_do_list.controller.TaskController;
import com.github.hu553in.to_do_list.test_util.AbstractDatabaseContainerTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationTest extends AbstractDatabaseContainerTest {

    @Autowired
    private TaskController taskController;

    @Test
    @DisplayName("given Spring Boot app - when run Spring Boot based test - then load application context")
    void testAppContextLoads() {

        // assert

        Assertions.assertThat(taskController).isNotNull();
    }

}
