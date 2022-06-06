package com.github.hu553in.to_do_list.service.impl;

import com.github.hu553in.to_do_list.dto.UserDto;
import com.github.hu553in.to_do_list.service.ICurrentUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService implements ICurrentUserService {

    @Override
    public UserDto getCurrentUser() {
        return new UserDto(SecurityContextHolder.getContext().getAuthentication());
    }

}
