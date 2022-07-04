package com.github.hu553in.to_do_list.mapper;

import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.entity.UserEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface UserEntityToDtoMapper extends Converter<UserEntity, UserDto> {

    @Override
    UserDto convert(@NonNull UserEntity source);

}
