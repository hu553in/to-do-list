package com.github.hu553in.to_do_list;

import com.github.hu553in.to_do_list.controller.TaskController;
import com.github.hu553in.to_do_list.test_util.AbstractSpringBootIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ApplicationTest extends AbstractSpringBootIntegrationTest {

    @Autowired
    private TaskController taskController;

    @Test
    void testLoadApplicationContext() {
        Assertions.assertThat(taskController).isNotNull();
    }

}
