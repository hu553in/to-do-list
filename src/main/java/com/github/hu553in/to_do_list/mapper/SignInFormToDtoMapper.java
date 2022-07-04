package com.github.hu553in.to_do_list.mapper;

import com.github.hu553in.to_do_list.dto.SignInDto;
import com.github.hu553in.to_do_list.form.SignInForm;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface SignInFormToDtoMapper extends Converter<SignInForm, SignInDto> {

    @Override
    SignInDto convert(@NonNull SignInForm source);

}
