package com.github.hu553in.to_do_list.service;

import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.enumeration.TaskStatus;
import com.github.hu553in.to_do_list.form.CreateTaskForm;
import com.github.hu553in.to_do_list.form.UpdateTaskForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITaskService {

    Page<TaskDto> getPageByStatus(TaskStatus status, Pageable pageable);

    TaskDto getById(Integer id);

    TaskDto create(CreateTaskForm form);

    void updateById(Integer id, UpdateTaskForm form);

    void deleteById(Integer id);

}
