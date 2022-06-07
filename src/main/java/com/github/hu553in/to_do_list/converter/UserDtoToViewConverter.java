package com.github.hu553in.to_do_list.converter;

import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.mapper.UserMapper;
import com.github.hu553in.to_do_list.view.UserView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToViewConverter implements Converter<UserDto, UserView> {

    @Override
    public UserView convert(@NonNull final UserDto source) {
        return UserMapper.INSTANCE.mapDtoToView(source);
    }

}
