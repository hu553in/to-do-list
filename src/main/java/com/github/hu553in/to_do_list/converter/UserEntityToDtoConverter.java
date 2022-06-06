package com.github.hu553in.to_do_list.converter;

import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.entity.UserEntity;
import com.github.hu553in.to_do_list.mapper.UserMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToDtoConverter implements Converter<UserEntity, UserDto> {

    @Override
    public UserDto convert(@NonNull final UserEntity source) {
        return UserMapper.INSTANCE.mapEntityToDto(source);
    }

}
