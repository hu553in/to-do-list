package com.github.hu553in.to_do_list.controller;

import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.enumeration.TaskSortableField;
import com.github.hu553in.to_do_list.enumeration.TaskStatus;
import com.github.hu553in.to_do_list.form.CreateTaskForm;
import com.github.hu553in.to_do_list.form.UpdateTaskForm;
import com.github.hu553in.to_do_list.service.ITaskService;
import com.github.hu553in.to_do_list.util.SwaggerConstants;
import com.github.hu553in.to_do_list.view.TaskView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@Tag(name = "Task", description = "The task API")
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService taskService;
    private final ConversionService conversionService;

    @Operation(security = @SecurityRequirement(name = SwaggerConstants.BEARER_JWT_AUTH_SECURITY_SCHEME_NAME))
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Collection<TaskView> getAll(
            @RequestParam(value = "status", defaultValue = "TO_DO") final TaskStatus status,
            @RequestParam(value = "sortBy", defaultValue = "TEXT") final TaskSortableField sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") final Sort.Direction sortDirection) {
        return taskService
                .getAll(status, sortBy, sortDirection)
                .stream()
                .map(it -> conversionService.convert(it, TaskView.class))
                .collect(Collectors.toSet());
    }

    @Operation(security = @SecurityRequirement(name = SwaggerConstants.BEARER_JWT_AUTH_SECURITY_SCHEME_NAME))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody final CreateTaskForm form) {
        TaskDto createdTask = taskService.create(form);
        URI location = URI.create("/task/" + createdTask.id());
        return ResponseEntity.created(location).build();
    }

    @Operation(security = @SecurityRequirement(name = SwaggerConstants.BEARER_JWT_AUTH_SECURITY_SCHEME_NAME))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TaskView getById(@PathVariable("id") final Integer id) {
        return conversionService.convert(taskService.getById(id), TaskView.class);
    }

    @Operation(security = @SecurityRequirement(name = SwaggerConstants.BEARER_JWT_AUTH_SECURITY_SCHEME_NAME))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") final Integer id) {
        taskService.delete(id);
    }

    @Operation(security = @SecurityRequirement(name = SwaggerConstants.BEARER_JWT_AUTH_SECURITY_SCHEME_NAME))
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") final Integer id, @Valid @RequestBody final UpdateTaskForm form) {
        taskService.update(id, form);
    }

}
