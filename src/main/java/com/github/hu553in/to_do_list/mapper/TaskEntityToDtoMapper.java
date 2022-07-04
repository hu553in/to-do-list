package com.github.hu553in.to_do_list.mapper;

import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring", uses = UserEntityToDtoMapper.class)
public interface TaskEntityToDtoMapper extends Converter<TaskEntity, TaskDto> {

    @Override
    TaskDto convert(@NonNull TaskEntity source);

}
