package com.github.hu553in.to_do_list.mapper;

import com.github.hu553in.to_do_list.dto.UpdateTaskDto;
import com.github.hu553in.to_do_list.form.UpdateTaskForm;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface UpdateTaskFormToDtoMapper extends Converter<UpdateTaskForm, UpdateTaskDto> {

    @Override
    UpdateTaskDto convert(@NonNull UpdateTaskForm source);

}
