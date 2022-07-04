package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.dto.CreateTaskDto;
import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.dto.UpdateTaskDto;
import com.github.hu553in.to_do_list.entity.TaskEntity;
import com.github.hu553in.to_do_list.entity.UserEntity;
import com.github.hu553in.to_do_list.enumeration.TaskStatus;
import com.github.hu553in.to_do_list.exception.NotFoundException;
import com.github.hu553in.to_do_list.exception.ServerErrorException;
import com.github.hu553in.to_do_list.exception.SortPropertyNotFoundException;
import com.github.hu553in.to_do_list.repository.jpa.TaskRepository;
import com.github.hu553in.to_do_list.repository.jpa.UserRepository;
import com.github.hu553in.to_do_list.service.ICurrentUserService;
import com.github.hu553in.to_do_list.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ICurrentUserService currentUserService;
    private final ConversionService conversionService;

    @Override
    public Page<TaskDto> getPageByStatus(final TaskStatus status, final Pageable pageable) {
        Integer currentUserId = currentUserService.getCurrentUser().id();
        try {
            return taskRepository
                    .findAllByStatusAndOwnerId(status, currentUserId, pageable)
                    .map(it -> conversionService.convert(it, TaskDto.class));
        } catch (PropertyReferenceException e) {
            throw new SortPropertyNotFoundException(e);
        }
    }

    @Override
    public TaskDto getById(final Integer id) {
        Integer currentUserId = currentUserService.getCurrentUser().id();
        return taskRepository
                .findByIdAndOwnerId(id, currentUserId)
                .map(it -> conversionService.convert(it, TaskDto.class))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public TaskDto create(final CreateTaskDto dto) {
        TaskEntity task = new TaskEntity();
        task.setText(dto.text());
        task.setStatus(TaskStatus.TO_DO);
        UserEntity currentUser = userRepository
                .findById(currentUserService.getCurrentUser().id())
                .orElseThrow(() -> new ServerErrorException("Current user is not found by ID"));
        task.setOwner(currentUser);
        TaskEntity createdTask = taskRepository.saveAndFlush(task);
        return conversionService.convert(createdTask, TaskDto.class);
    }

    @Override
    @Transactional
    public void updateById(final Integer id, final UpdateTaskDto dto) {
        Integer currentUserId = currentUserService.getCurrentUser().id();
        TaskEntity task = taskRepository
                .findByIdAndOwnerId(id, currentUserId)
                .orElseThrow(NotFoundException::new);
        Optional
                .ofNullable(dto.text())
                .ifPresent(task::setText);
        Optional
                .ofNullable(dto.status())
                .ifPresent(task::setStatus);
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional
    public void deleteById(final Integer id) {
        Integer currentUserId = currentUserService.getCurrentUser().id();
        taskRepository
                .deleteByIdAndOwnerId(id, currentUserId)
                .orElseThrow(NotFoundException::new);
    }

}
