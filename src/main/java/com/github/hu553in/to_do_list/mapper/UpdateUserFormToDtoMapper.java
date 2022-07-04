package com.github.hu553in.to_do_list.mapper;

import com.github.hu553in.to_do_list.dto.UpdateUserDto;
import com.github.hu553in.to_do_list.form.UpdateUserForm;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface UpdateUserFormToDtoMapper extends Converter<UpdateUserForm, UpdateUserDto> {

    @Override
    UpdateUserDto convert(@NonNull UpdateUserForm source);

}
