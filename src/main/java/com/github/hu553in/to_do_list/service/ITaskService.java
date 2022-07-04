package com.github.hu553in.to_do_list.service;

import com.github.hu553in.to_do_list.dto.CreateTaskDto;
import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.dto.UpdateTaskDto;
import com.github.hu553in.to_do_list.enumeration.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITaskService {

    Page<TaskDto> getPageByStatus(TaskStatus status, Pageable pageable);

    TaskDto getById(Integer id);

    TaskDto create(CreateTaskDto dto);

    void updateById(Integer id, UpdateTaskDto dto);

    void deleteById(Integer id);

}
