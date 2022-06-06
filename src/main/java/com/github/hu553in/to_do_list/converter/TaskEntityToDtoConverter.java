package com.github.hu553in.to_do_list.converter;

import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.entity.TaskEntity;
import com.github.hu553in.to_do_list.mapper.TaskMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TaskEntityToDtoConverter implements Converter<TaskEntity, TaskDto> {

    @Override
    public TaskDto convert(@NonNull final TaskEntity source) {
        return TaskMapper.INSTANCE.mapEntityToDto(source);
    }

}
