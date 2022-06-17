package com.github.hu553in.to_do_list.service;

import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.form.UpdateUserForm;

import java.util.Collection;

public interface IUserService {

    Collection<UserDto> findAll();

    UserDto getById(Integer id);

    void update(Integer id, UpdateUserForm form);

}
