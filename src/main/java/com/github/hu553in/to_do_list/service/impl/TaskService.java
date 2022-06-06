package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.entity.TaskEntity;
import com.github.hu553in.to_do_list.exception.NotFoundException;
import com.github.hu553in.to_do_list.form.CreateTaskForm;
import com.github.hu553in.to_do_list.form.UpdateTaskForm;
import com.github.hu553in.to_do_list.model.TaskSortableField;
import com.github.hu553in.to_do_list.model.TaskStatus;
import com.github.hu553in.to_do_list.repository.jpa.TaskRepository;
import com.github.hu553in.to_do_list.repository.jpa.UserRepository;
import com.github.hu553in.to_do_list.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final ConversionService conversionService;

    @Override
    public Collection<TaskDto> getAll(final TaskStatus status,
                                      final TaskSortableField sortBy,
                                      final Sort.Direction sortDirection) {
        Sort sort = Sort.by(sortDirection, sortBy.toString());
        Integer currentUserId = currentUserService.getCurrentUser().getId();
        return taskRepository
                .findAllByStatusAndOwnerId(status, currentUserId, sort)
                .stream()
                .map(it -> conversionService.convert(it, TaskDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto create(final CreateTaskForm form) {
        TaskEntity task = new TaskEntity();
        task.setText(form.text());
        task.setStatus(TaskStatus.TO_DO);
        Instant now = Instant.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        Integer currentUserId = currentUserService.getCurrentUser().getId();
        task.setOwner(userRepository.getReferenceById(currentUserId));
        TaskEntity createdTask = taskRepository.saveAndFlush(task);
        return conversionService.convert(createdTask, TaskDto.class);
    }

    @Override
    public TaskDto getById(final Integer id) {
        Integer currentUserId = currentUserService.getCurrentUser().getId();
        return taskRepository
                .findByIdAndOwnerId(id, currentUserId)
                .map(it -> conversionService.convert(it, TaskDto.class))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void update(final Integer id, final UpdateTaskForm form) {
        Integer currentUserId = currentUserService.getCurrentUser().getId();
        TaskEntity task = taskRepository
                .findByIdAndOwnerId(id, currentUserId)
                .orElseThrow(NotFoundException::new);
        String text = form.text();
        boolean updated = false;
        if (text != null) {
            task.setText(text);
            updated = true;
        }
        TaskStatus status = form.status();
        if (status != null) {
            task.setStatus(status);
            updated = true;
        }
        if (updated) {
            task.setUpdatedAt(Instant.now());
            taskRepository.saveAndFlush(task);
        }
    }

    @Override
    @Transactional
    public void delete(final Integer id) {
        Integer currentUserId = currentUserService.getCurrentUser().getId();
        taskRepository
                .deleteByIdAndOwnerId(id, currentUserId)
                .orElseThrow(NotFoundException::new);
    }

}
