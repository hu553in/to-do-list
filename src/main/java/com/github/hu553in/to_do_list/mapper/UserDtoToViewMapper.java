package com.github.hu553in.to_do_list.mapper;

import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.view.UserView;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface UserDtoToViewMapper extends Converter<UserDto, UserView> {

    @Override
    UserView convert(@NonNull UserDto source);

}
