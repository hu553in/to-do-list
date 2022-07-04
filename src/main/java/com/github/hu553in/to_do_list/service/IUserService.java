package com.github.hu553in.to_do_list.service;

import com.github.hu553in.to_do_list.dto.UpdateUserDto;
import com.github.hu553in.to_do_list.dto.UserDto;

import java.util.Collection;

public interface IUserService {

    Collection<UserDto> getAll();

    UserDto getById(Integer id);

    void updateById(Integer id, UpdateUserDto dto);

}
