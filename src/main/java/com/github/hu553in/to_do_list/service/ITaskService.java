package com.github.hu553in.to_do_list.service;

import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.form.CreateTaskForm;
import com.github.hu553in.to_do_list.form.UpdateTaskForm;
import com.github.hu553in.to_do_list.model.TaskSortableField;
import com.github.hu553in.to_do_list.model.TaskStatus;
import org.springframework.data.domain.Sort;

import java.util.Collection;

public interface ITaskService {

    Collection<TaskDto> getAll(TaskStatus status, TaskSortableField sortBy, Sort.Direction sortDirection);

    TaskDto create(CreateTaskForm form);

    TaskDto getById(Integer id);

    void update(Integer id, UpdateTaskForm form);

    void delete(Integer id);

}