package com.github.hu553in.to_do_list.mapper;

import com.github.hu553in.to_do_list.dto.CreateTaskDto;
import com.github.hu553in.to_do_list.form.CreateTaskForm;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface CreateTaskFormToDtoMapper extends Converter<CreateTaskForm, CreateTaskDto> {

    @Override
    CreateTaskDto convert(@NonNull CreateTaskForm source);

}
