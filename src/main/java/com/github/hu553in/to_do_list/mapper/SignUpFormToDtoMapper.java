package com.github.hu553in.to_do_list.mapper;

import com.github.hu553in.to_do_list.dto.SignUpDto;
import com.github.hu553in.to_do_list.form.SignUpForm;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface SignUpFormToDtoMapper extends Converter<SignUpForm, SignUpDto> {

    @Override
    SignUpDto convert(@NonNull SignUpForm source);

}
