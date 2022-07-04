package com.github.hu553in.to_do_list.mapper;

import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.view.TaskView;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring", uses = UserEntityToDtoMapper.class)
public interface TaskDtoToViewMapper extends Converter<TaskDto, TaskView> {

    @Override
    TaskView convert(@NonNull TaskDto source);

}
